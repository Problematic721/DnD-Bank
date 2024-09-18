package com.example.dndbank.repository;

import com.example.dndbank.model.Campaign;
import com.example.dndbank.model.User;
import com.example.dndbank.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
	List<Wallet> findByUserId(Long userId);
	List<Wallet> findByUser(User user);
	List<Wallet> findByUserAndCampaign(User user, Campaign campaign);
	List<Wallet> findByCampaign(Campaign campaign);
}