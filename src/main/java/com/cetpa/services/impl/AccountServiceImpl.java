package com.cetpa.services.impl;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cetpa.entities.Account;
import com.cetpa.entities.TransactionSummary;
import com.cetpa.repositories.AccountRepository;
import com.cetpa.repositories.SummaryRepository;
import com.cetpa.services.AccountService;

@Service
public class AccountServiceImpl implements AccountService 
{
	@Autowired private AccountRepository accountRepository;
	@Autowired private SummaryRepository summaryRepository;

	public Account getAccount(String userid) 
	{
		Account account=accountRepository.findByUserid(userid);
		return account;
	}
	public int getAmount(int accountno) 
	{
		int amount=accountRepository.getAmount(accountno);
		return amount;
	}
	public void updateAmount(TransactionSummary summary) 
	{
		accountRepository.updateBalance(summary.getAmount(),summary.getAccountno());
		summary.setDate(LocalDate.now().toString());
		LocalTime time=LocalTime.now();
		summary.setTime(time.getHour()+":"+time.getMinute()+":"+time.getSecond());
		summary.setTransactionType("Credit");
		summaryRepository.save(summary);
	}
	public boolean debitAmount(TransactionSummary summary) 
	{
		int an=summary.getAccountno();
		int camount=accountRepository.getAmount(an);
		int wamount=summary.getAmount();
		if(wamount>camount)
			return false;
		accountRepository.updateBalance(-wamount,an);
		summary.setDate(LocalDate.now().toString());
		LocalTime time=LocalTime.now();
		summary.setTime(time.getHour()+":"+time.getMinute()+":"+time.getSecond());
		summary.setTransactionType("Debit");
		summaryRepository.save(summary);
		return true;
	}
	public Account getAccount(int accountno) 
	{
		return accountRepository.findById(accountno).orElse(null);
	}

	public boolean deductFromAccount(int acc, double amount) {
		int camount=accountRepository.getAmount(acc);
		if(amount>camount) {
			return false;
		}
		accountRepository.updateBalance((int)-amount,acc);
		return true;
	}
	public boolean addToAccount(String receiverAccountId, double amount) {
		accountRepository.updateBalance((int)+amount,Integer.parseInt(receiverAccountId));
		return true;
	}
}
