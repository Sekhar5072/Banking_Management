package com.jsp.bms.service.impl;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.jsp.bms.entity.TransactionDetails;
import com.jsp.bms.entity.UserDetails;
import com.jsp.bms.repository.BankingRepository;
import com.jsp.bms.repository.TransactionRepository;
import com.jsp.bms.service.BankingService;

@Service
public class BankingServiceImpl implements BankingService{

	private BankingRepository bankingRepository;
	private TransactionRepository transactionRepository;
	
	public BankingServiceImpl(BankingRepository bankingRepository, TransactionRepository transactionRepository) {
		super();
		this.bankingRepository = bankingRepository;
		this.transactionRepository = transactionRepository;
	}

	public String getAccountNo() {
		Random random = new Random();
		int accNo = random.nextInt(1000000000);
		if (accNo < 100000000) {
			accNo += 100000000;
		}
		return "OP-"+accNo;
	}
	
	@Override
	public UserDetails saveUserDetails(UserDetails details) {
		String accNo = getAccountNo();
		details.setAccountNo(accNo);
		return bankingRepository.save(details);
	}

	@Override
	public UserDetails performLogin(String accountNo, String password) {
		try {
			UserDetails details = bankingRepository.findByAccountNoAndPassword(accountNo, password);
			return details;
		}
		catch (Exception e) {
			return null;
		}	
	}

	@Override
	public UserDetails getDetails(String accountNo) {
		UserDetails details = bankingRepository.findByAccountNo(accountNo);
		return details;
	}

	@Override
	public UserDetails updateDetails(UserDetails details) {
		return bankingRepository.save(details);
	}

	public String getTransactionId() {
		Random random = new Random();
		int transactionNo = random.nextInt(100000);
		if (transactionNo < 10000) {
			transactionNo += 10000;
		}
		return "TID-"+transactionNo;
	}

	@Override
	public TransactionDetails saveTranactionDetails(TransactionDetails tDetails) {
		String transactionId = getTransactionId();
		tDetails.setTid(transactionId);
		return transactionRepository.save(tDetails);
	}

	@Override
	public List<TransactionDetails> getAllTransactionDetails(String senderAccountNo, String receiverAccountNo) {
		return transactionRepository.findBySenderAccountNoOrReceiverAccountNoOrderByTransactionDateDesc(senderAccountNo, receiverAccountNo);
	}
}