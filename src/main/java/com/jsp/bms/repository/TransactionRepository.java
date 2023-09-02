package com.jsp.bms.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.jsp.bms.entity.TransactionDetails;

public interface TransactionRepository extends CrudRepository<TransactionDetails, String>{
	
	List<TransactionDetails> findBySenderAccountNoOrReceiverAccountNoOrderByTransactionDateDesc(String senderAccountNo, String receiverAccountNo);
	
}