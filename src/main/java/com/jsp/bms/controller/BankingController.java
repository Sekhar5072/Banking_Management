package com.jsp.bms.controller;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jsp.bms.entity.TransactionDetails;
import com.jsp.bms.entity.UserDetails;
import com.jsp.bms.service.BankingService;

@Controller
public class BankingController {
	
	@Autowired
	private BankingService bankingService;
	
	//welcome page
	@GetMapping("/welcome")
	public String welcome() {
		return "index";
	}
	
	//login form
	@GetMapping("/welcome/login")
	public String loginForm() {
		return "Login";
	}
	
	//register form
	@GetMapping("/welcome/register")
	public String registerForm(Model model) {
		UserDetails details = new UserDetails();
		model.addAttribute("details", details);
		return "CreateAccount";
	}
	
	//to fetch data from registration form
	@PostMapping("/registration")
	public String InsertRegisterFormData(@ModelAttribute("userDetails") @RequestParam String cfPassword, UserDetails details, Model model) {
		if (details.getPassword().equals(cfPassword)) {
			bankingService.saveUserDetails(details);
			return "DisplayAccount";
		}
		else {
			model.addAttribute("errorPassword", "Both Passwords are not Matching. Please Enter Correct Passwords");
			return "CreateAccount";
		}
	}
	
	//login operation
	@PostMapping("/loginOperation")
	public String logInForm(@RequestParam String accountNo, @RequestParam String password, Model model) {
		UserDetails details = bankingService.performLogin(accountNo, password);
		if (details != null) {
			model.addAttribute("details", details);
			return "DisplayHome";
		}
		else {
			model.addAttribute("errorMsg", "Oops! You might have entered wrong Account Number or Password. Please Enter Valid Details");
			return "Login";
		}
	}
	
	//view balance
	@GetMapping("/loginOperation/displayBalance/{accountNo}")
	public String displayBalance(@PathVariable String accountNo, Model model) {
		model.addAttribute("details", bankingService.getDetails(accountNo));
		return "ViewBalance";
	}
	
	//to deposit money
	@GetMapping("/loginOperation/depositCash/{accountNo}")
	public String depositCash(@PathVariable String accountNo, Model model) {
		model.addAttribute("details", bankingService.getDetails(accountNo));
		return "DepositCash";
	}
	
	//update balance after depositing
	@PostMapping("/loginOperation/deposit/{accountNo}")
	public String updateBalance(@PathVariable String accountNo, UserDetails details , Model model) {
		StringBuilder builder = new StringBuilder();
		//to get details from database by accountNo
		UserDetails updateDetails = bankingService.getDetails(accountNo);
		double bal = updateDetails.getBalance();
		updateDetails.setBalance(details.getBalance() + bal);
		builder.append("Cash Deposited Successfully. Your Balance is : " +(details.getBalance()+bal));
		//save updated details
		bankingService.updateDetails(updateDetails);
		model.addAttribute("msg", builder);
		model.addAttribute("details", details);
		return "DepositCash";
	}
	
	//to withdraw money
	@GetMapping("/loginOperation/withdrawMoney/{accountNo}")
	public String withdraw(@PathVariable String accountNo, Model model) {
		model.addAttribute("details", bankingService.getDetails(accountNo));
		return "WithdrawCash";
	}
	
	//update balance after withdraw
	@PostMapping("/loginOperation/withdraw/{accountNo}")
	public String withdrawMoney(@PathVariable String accountNo, UserDetails details , Model model) {
		StringBuilder builder = new StringBuilder();
		boolean updated = false;
		//to get details from database by accountNo
		UserDetails updateDetails = bankingService.getDetails(accountNo);
		double bal = updateDetails.getBalance();
		if (details.getBalance() < updateDetails.getBalance()) {
			updateDetails.setBalance(bal - details.getBalance());
			updated = true;
			builder.append("Cash Withdrawl Successfully. Your Remaining Balance is : " + (bal - details.getBalance()));
		}
		else {
			builder.append("You dont have Enough Amount to withdraw !!!  Your Balance is only : " + bal);
		}
		
		//save details
		if (updated) {
			//save updated details
			bankingService.updateDetails(updateDetails);
			model.addAttribute("msg", builder);
		}
		else {
			model.addAttribute("errorMsg", builder);
		}
		model.addAttribute("details", details);
		return "WithdrawCash";
		
	}
	
	//to view profile
	@GetMapping("/loginOperation/viewProfile/{accountNo}")
	public String viewProfile(@PathVariable String accountNo, Model model) {
		model.addAttribute("details", bankingService.getDetails(accountNo));
		return "ViewProfile";
	}
	
	//to update mobile number
	@PostMapping("/loginOperation/update/{accountNo}")
	public String updateMobileAndAddressAndPassword(@PathVariable String accountNo, UserDetails details, Model model) {
		UserDetails details2 = bankingService.getDetails(accountNo);
		boolean updated = false;
		StringBuilder builder = new StringBuilder();
		
		if (details.getMobileNo() != null) {
			details2.setMobileNo(details.getMobileNo());
			updated = true;
			builder.append("Your Mobile Number Updated Successfully");
			//model.addAttribute("msg", "Your Mobile Number Updated Successfully...");
		}
		else if(details.getAddress() != null) {
			details2.setAddress(details.getAddress());
			updated = true;
			builder.append("Your Address Updated Successfully");
			//model.addAttribute("msg", "Your Address Updated Successfully...");
		}
		else if(details.getPassword() != null) {
			details2.setPassword(details.getPassword());
			updated = true;
			builder.append("Your Password Updated Successfully");
			//model.addAttribute("msg", "Your Password Updated Successfully...");
		}
		
		//to save details
		if (updated) {
			bankingService.updateDetails(details2);
			model.addAttribute("msg", builder);
		}
		model.addAttribute("details", details2);
		return "ViewProfile";
	}
	
	//to transfer money
	@GetMapping("/loginOperation/transfer/{accountNo}")
	public String transferMoney(@PathVariable String accountNo, Model model) {
		model.addAttribute("details", bankingService.getDetails(accountNo));
		return "TransferMoney";
	}
	
	//to get above form details as well as to update and save details
	@PostMapping("/loginOperation/transferOperation/{accountNo}")
	public String transferOperation(@PathVariable String accountNo, @RequestParam String accNo, @RequestParam double amount , UserDetails details, Model model) {
		StringBuilder builder = new StringBuilder();
		boolean updated = false;
		UserDetails senderDetails = bankingService.getDetails(accountNo);
		UserDetails recevierDetails = bankingService.getDetails(accNo);
		double bal = senderDetails.getBalance();
		if (bal > amount) {
			//to update
			recevierDetails.setBalance(recevierDetails.getBalance() + amount);
			senderDetails.setBalance(bal - amount);
			updated = true;
			builder.append("Money Transfer Successfull. And your Remaining balance is : Rs. " + senderDetails.getBalance());
			
			//to save date
			Date date = new Date(); //to get current system data for transaction
			SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-YYYY"); //for proper format
			String transactionDate = dateformat.format(date);
			
			//to save transaction details
			TransactionDetails tDetails = new TransactionDetails();
			tDetails.setSenderAccountNo(accountNo);
			tDetails.setReceiverAccountNo(accNo);
			tDetails.setAmount(amount);
			tDetails.setTransactionDate(transactionDate);
			
			//save
			bankingService.saveTranactionDetails(tDetails);
			
		}
		else {
			builder.append("You dont have enough balance to Transfer. You have only : Rs. " + senderDetails.getBalance());
		}
		
		//to save details
		if (updated) {
			bankingService.updateDetails(senderDetails);
			bankingService.updateDetails(recevierDetails);
			model.addAttribute("msg", builder);
		}
		else {
			model.addAttribute("Errormsg", builder);
		}
		model.addAttribute("details", senderDetails);
		return "TransferMoney";
	}
	
	//to get transaction history
	@GetMapping("/loginOperation/transactionHistory/{accountNo}")
	public String transactionHistory(@PathVariable String accountNo,UserDetails details , Model model) {
		List<TransactionDetails> transactions = bankingService.getAllTransactionDetails(details.getAccountNo(), details.getAccountNo());
		model.addAttribute("transactions", transactions);
		return "ViewTransactionHistory";
	}	
}