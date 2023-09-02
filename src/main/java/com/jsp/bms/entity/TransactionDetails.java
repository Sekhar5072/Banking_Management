package com.jsp.bms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactiondetails")
public class TransactionDetails {
	@Id
	private String tid;
	private String senderAccountNo;
	private String receiverAccountNo;
	private double amount;
	private String transactionDate;
	
	public TransactionDetails() {
		
	}

	public TransactionDetails(String tid, String senderAccountNo, String receiverAccountNo, double amount,
			String transactionDate) {
		super();
		this.tid = tid;
		this.senderAccountNo = senderAccountNo;
		this.receiverAccountNo = receiverAccountNo;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}
	
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getSenderAccountNo() {
		return senderAccountNo;
	}
	public void setSenderAccountNo(String senderAccountNo) {
		this.senderAccountNo = senderAccountNo;
	}
	public String getReceiverAccountNo() {
		return receiverAccountNo;
	}
	public void setReceiverAccountNo(String receiverAccountNo) {
		this.receiverAccountNo = receiverAccountNo;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
}
