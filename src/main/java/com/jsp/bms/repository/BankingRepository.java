package com.jsp.bms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.bms.entity.UserDetails;

public interface BankingRepository extends JpaRepository<UserDetails, String>{
	UserDetails findByAccountNoAndPassword(String accountNo, String password);
	
	UserDetails findByAccountNo(String accountNo);
}