package com.example.dndbank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dndbank.model.Campaign;
import com.example.dndbank.model.Transaction;
import com.example.dndbank.model.User;
import com.example.dndbank.model.Wallet;
import com.example.dndbank.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private WalletService walletService;

    public List<Transaction> getTransactionsForUsers(User user, Campaign campaign) {
    	List<Wallet> userWallets = walletService.findByUserAndCampaign(user, campaign);
    	
    	List<Transaction> allTransactionsByUser = new ArrayList<>();
    	for (Wallet wallet : userWallets) {
            allTransactionsByUser.addAll(transactionRepository.findByFromWallet(wallet));
            allTransactionsByUser.addAll(transactionRepository.findByToWallet(wallet));
        }
    	return allTransactionsByUser;
    }

    public List<Transaction> getTransactionsForDM(Campaign campaign) {
        return transactionRepository.findByCampaign(campaign);
    }
    
    @Transactional
	public void saveTransaction(Wallet fromWallet, Wallet toWallet, int copper, int silver, int gold, int platinum,
			Campaign campaign) {
		Transaction transaction = new Transaction(fromWallet, toWallet, copper, silver, gold, platinum, campaign);
		transactionRepository.save(transaction);
		
	}
}