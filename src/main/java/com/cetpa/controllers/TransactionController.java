package com.cetpa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cetpa.entities.Account;
import com.cetpa.entities.TransactionSummary;
import com.cetpa.services.AccountService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/canara-bank/transaction")
public class TransactionController {
	@Autowired
	private AccountService accountService;

	@RequestMapping("balance")
	public String getShowBalanceView(Model model, HttpSession ses) {
		int amount = accountService.getAmount((Integer) ses.getAttribute("accountno"));
		model.addAttribute("amount", amount);
		return "transaction/show-balance";
	}

	@RequestMapping("deposit")
	public String getDepositMoneyView() {
		return "transaction/deposit-money";
	}

	@RequestMapping("deposit-amount")
	public String depositAcount(TransactionSummary summary, HttpSession ses, Model model) {
		summary.setAccountno((Integer) ses.getAttribute("accountno"));
		accountService.updateAmount(summary);
		model.addAttribute("amount", summary.getAmount());
		return "transaction/deposit-success";
	}

	@RequestMapping("withdraw")
	public String getWithdrawMoneyView() {
		return "transaction/withdraw-money";
	}

	@RequestMapping("withdraw-amount")
	public String withdrawAcount(TransactionSummary summary, HttpSession ses, Model model) {
		summary.setAccountno((Integer) ses.getAttribute("accountno"));
		boolean success = accountService.debitAmount(summary);
		if (!success) {
			model.addAttribute("amount", summary.getAmount());
			model.addAttribute("msg", "You do not have sufficient balance");
			return "transaction/withdraw-money";
		}
		model.addAttribute("amount", summary.getAmount());
		return "transaction/withdraw-success";
	}

	@RequestMapping("reciever")
	public String getRecieverAccountNoView() {
		return "transaction/reciever-accountno";
	}

	@RequestMapping("transfer")
	public String getTransferView(int accountno, Model model) {
		Account account = accountService.getAccount(accountno);
		if (account == null) {
			model.addAttribute("accountno", accountno);
			model.addAttribute("msg", "Entered account number does not exist");
			return "transaction/reciever-accountno";
		}
		model.addAttribute("name", account.getUserid());
		return "transaction/transfer-money";
	}
	@RequestMapping("update-transfer-amount")					
	public String updateTransferAmount(@RequestParam("accountno") String accountno,@RequestParam("wamount") double wamount) {
		boolean transactionSuccess = performTransaction(UserController.acc, accountno, wamount);
		if (transactionSuccess) {
			return "transaction/transSuccess";
		} else {
			return "transaction/transFail";
		}
	}
	private boolean performTransaction(int acc, String receiverAccountId, double amount) {
		boolean senderSuccess = accountService.deductFromAccount(acc, amount);
        boolean receiverSuccess = accountService.addToAccount(receiverAccountId, amount);
        
        return senderSuccess && receiverSuccess;
	}

}
