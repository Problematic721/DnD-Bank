package com.example.dndbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dndbank.model.Campaign;
import com.example.dndbank.model.Transaction;
import com.example.dndbank.model.User;
import com.example.dndbank.model.Wallet;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByFromWallet(Wallet fromWallet);

	List<Transaction> findByToWallet(Wallet toWallet);

	List<Transaction> findByCampaign(Campaign campaign);

}
