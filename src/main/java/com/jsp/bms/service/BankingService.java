package com.jsp.bms.service;


import java.util.List;

import com.jsp.bms.entity.TransactionDetails;
import com.jsp.bms.entity.UserDetails;

public interface BankingService {
	UserDetails saveUserDetails(UserDetails details);
	
	UserDetails performLogin(String accountNo,String password);
	
	UserDetails getDetails(String accountNo);
	
	UserDetails updateDetails(UserDetails details);
	
	TransactionDetails saveTranactionDetails(TransactionDetails tDetails);
	
	List<TransactionDetails> getAllTransactionDetails(String senderAccountNo, String receiverAccountNo);
}
