package com.cetpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cetpa.entities.Account;

import jakarta.transaction.Transactional;

public interface AccountRepository extends JpaRepository<Account,Integer> 
{
	Account findByUserid(String uid);
	@Query("select amount from Account where accountno=:arg")
	int getAmount(@Param("arg") int an);
	
	@Transactional
	@Modifying	
	@Query("update Account set amount=amount+:arg1 where accountno=:arg2")
	void updateBalance(@Param("arg1") int amount,@Param("arg2") int accountno);
}
