package com.example.dndbank.service;

import com.example.dndbank.model.Campaign;
import com.example.dndbank.model.User;
import com.example.dndbank.model.Wallet;
import com.example.dndbank.repository.UserRepository;
import com.example.dndbank.repository.WalletRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private UserRepository userRepository;

	public List<Wallet> findByUser(User user) {
		return walletRepository.findByUser(user);
	}

	public Wallet createWallet(Wallet wallet) {
		return walletRepository.save(wallet);
	}

	public List<Wallet> findByUserAndCampaign(User user, Campaign campaign) {
		return walletRepository.findByUserAndCampaign(user, campaign);
	}

	@Transactional
	public void sendCoins(Long fromWalletId, Long toWalletId, int copper, int silver, int gold, int platinum,
			UserDetails userDetails) {
		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));
		Wallet fromWallet = walletRepository.findById(fromWalletId)
				.orElseThrow(() -> new RuntimeException("Sender wallet not found"));
		Wallet toWallet = walletRepository.findById(toWalletId)
				.orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

		Campaign campaign = fromWallet.getCampaign();
		boolean isDm = campaign.getDm().equals(currentUser);
		boolean isRecipientDm = campaign.getDm().equals(toWallet.getUser());

		if (!isDm) {
			if (fromWallet.getCopper() < copper || fromWallet.getSilver() < silver || fromWallet.getGold() < gold
					|| fromWallet.getPlatinum() < platinum) {
				throw new RuntimeException("Insufficient funds");
			}

			fromWallet.setCopper(fromWallet.getCopper() - copper);
			fromWallet.setSilver(fromWallet.getSilver() - silver);
			fromWallet.setGold(fromWallet.getGold() - gold);
			fromWallet.setPlatinum(fromWallet.getPlatinum() - platinum);
		}
		if (!isRecipientDm) {
			toWallet.setCopper(toWallet.getCopper() + copper);
			toWallet.setSilver(toWallet.getSilver() + silver);
			toWallet.setGold(toWallet.getGold() + gold);
			toWallet.setPlatinum(toWallet.getPlatinum() + platinum);
		}
		// Save changes to database
		walletRepository.save(fromWallet);
		walletRepository.save(toWallet);
	}

	public List<Wallet> findByCampaign(Campaign campaign) {
		return walletRepository.findByCampaign(campaign);
	}
	
	public Wallet findById(Long userId) {
		return   walletRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Wallet not found"));
	}
}