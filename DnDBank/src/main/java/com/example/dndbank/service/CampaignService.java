package com.example.dndbank.service;

import com.example.dndbank.model.Campaign;
import com.example.dndbank.model.User;
import com.example.dndbank.model.Wallet;
import com.example.dndbank.repository.CampaignRepository;
import com.example.dndbank.repository.UserRepository;
import com.example.dndbank.repository.WalletRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CampaignService {

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private WalletRepository walletRepository;

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CODE_LENGTH = 6;
	private static final SecureRandom RANDOM = new SecureRandom();

	public Campaign createCampaign(Campaign campaign) {
		return campaignRepository.save(campaign);
	}

	public Optional<Campaign> getCampaign(Long id) {
		return campaignRepository.findById(id);
	}

	public Campaign createCampaign(String campaignName, User user) {
		Campaign campaign = new Campaign();
		campaign.setName(campaignName);
		campaign.setDm(user);
		campaign.getUsers().add(user);
		campaign.setJoinCode(generateUniqueJoinCode());
		Campaign savedCampaign = campaignRepository.save(campaign);
		Wallet dmWallet = new Wallet();
        dmWallet.setUser(user);
        dmWallet.setName("DM Wallet");
        dmWallet.setCampaign(savedCampaign);
        walletRepository.save(dmWallet);
		return savedCampaign;
	}

	public String joinCampaign(String joinCode, User user) {
		Optional<Campaign> campaignOptional = campaignRepository.findByJoinCode(joinCode);
		if (campaignOptional.isPresent()) {
			Campaign campaign = campaignOptional.get();
			if (!campaign.getUsers().contains(user)) {
				campaign.getUsers().add(user);
				campaignRepository.save(campaign);
				user.getCampaigns().add(campaign);
				userRepository.save(user);
				Wallet wallet = new Wallet();
		        wallet.setName("Personal Wallet");
		        wallet.setUser(user);
		        wallet.setCampaign(campaign);
		        walletRepository.save(wallet);

				return "Successfully joined the campaign.";
			} else {
				return "You are already part of this campaign.";
			}
		} else {
			return "Invalid campaign code.";
		}
	}
	
	public Campaign regenerateJoinCode (Long campaignId) {
		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(()->new RuntimeException("Campaign Not Found"));
		
		String newJoinCode = generateUniqueJoinCode();
		campaign.setJoinCode(newJoinCode);
		campaignRepository.save(campaign);
		return campaign;
	    
	}

	private String generateUniqueJoinCode() {
		StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);
		for (int i = 0; i < CODE_LENGTH; i++) {
			int index = RANDOM.nextInt(CHARACTERS.length());
			codeBuilder.append(CHARACTERS.charAt(index));
		}
		return codeBuilder.toString();
	}

	public List<Campaign> getCampaignsByUsername(String username) {
		return campaignRepository.findByUsersUsername(username);
	}

	public List<User> getAllPlayerById(Long campaignId) {
		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(() -> new RuntimeException("Campaign not found"));

		User dm = campaign.getDm();
		Set<User> allUsers = campaign.getUsers();

		return allUsers.stream().filter(user -> !user.getId().equals(dm.getId())).collect(Collectors.toList());
	}

	public Campaign getCampaignById(Long campaignId) {
		return campaignRepository.findById(campaignId).orElseThrow(() -> new IllegalArgumentException("Invalid campaign ID"));
	}

	public void kickUser(Long campaignId, Long userId, User currentUser) {
		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid campaign ID"));

		if (!campaign.getDm().equals(currentUser)) {
			throw new RuntimeException("You are not the DM of this campaign");
		}

		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

		campaign.getUsers().remove(user);
		campaignRepository.save(campaign);
	}

	public void deleteCampaign(Long campaignId) {
		 Campaign campaign = campaignRepository.findById(campaignId)
			        .orElseThrow(() -> new RuntimeException("Campaign not found"));

		for (User user : campaign.getUsers()) {
	        user.getCampaigns().remove(campaign);
	        userRepository.save(user); // Save the updated user
	    }
		campaignRepository.deleteById(campaignId);
	}

	public void updateCampaign(Long campaignId, String newCampaignName) {
		Campaign campaign = campaignRepository.findById(campaignId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid campaign ID"));
		campaign.setName(newCampaignName);
		campaignRepository.save(campaign);

	}
}