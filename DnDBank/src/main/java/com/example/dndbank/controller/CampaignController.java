package com.example.dndbank.controller;

import com.example.dndbank.model.Campaign;
import com.example.dndbank.model.Transaction;
import com.example.dndbank.model.User;
import com.example.dndbank.model.Wallet;
import com.example.dndbank.service.CampaignService;
import com.example.dndbank.service.TransactionService;
import com.example.dndbank.service.UserService;
import com.example.dndbank.service.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class CampaignController {

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private UserService userService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/campaign")
	public String showCampaigns(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		String username = userDetails.getUsername();
		List<Campaign> campaigns = campaignService.getCampaignsByUsername(username);
		model.addAttribute("campaigns", campaigns);
		return "campaign";
	}

	@PostMapping("/campaign/create")
	public String createCampaign(@RequestParam String campaignName, @AuthenticationPrincipal UserDetails userDetails) {
		String username = userDetails.getUsername();
		User user = userService.findByUsername(username);
		campaignService.createCampaign(campaignName, user);
		return "redirect:/campaign";
	}

	@PostMapping("/campaign/join")
	public String joinCampaign(@RequestParam String joinCode, @AuthenticationPrincipal UserDetails userDetails) {
		String username = userDetails.getUsername();
		User user = userService.findByUsername(username);
		campaignService.joinCampaign(joinCode, user);
		return "redirect:/campaign";
	}

	@GetMapping("/campaign/{id}")
	public String viewCampaignDetail(@PathVariable Long id, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findByUsername(userDetails.getUsername());
		Campaign campaign = campaignService.getCampaignById(id);
		List<Wallet> wallets;
		List<Transaction> transactions;
		boolean isDm = campaign.getDm().equals(user);
		if (!isDm) {
			wallets = walletService.findByUserAndCampaign(user, campaign);
			transactions = transactionService.getTransactionsForDM(campaign);
		} else {
			wallets = walletService.findByCampaign(campaign);
			transactions = transactionService.getTransactionsForUsers(user, campaign);
		}
		model.addAttribute("campaign", campaign);
		model.addAttribute("users", campaignService.getAllPlayerById(id));
		model.addAttribute("dm", campaign.getDm());
		model.addAttribute("wallets", wallets);
		model.addAttribute("transactions", transactions);
		return "campaign_details";
	}

	@PostMapping("/campaign/{id}/kick/{userId}")
	public String kickPlayer(@PathVariable Long id, @PathVariable Long userId,
			@AuthenticationPrincipal UserDetails userDetails) {
		String currentUsername = userDetails.getUsername();
		User currentUser = userService.findByUsername(currentUsername);
		campaignService.kickUser(id, userId, currentUser);
		return "redirect:/campaign/" + id;
	}

	@PostMapping("/campaign/{id}/delete")
	public String deleteCampaign(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		Campaign campaign = campaignService.getCampaignById(id);

		if (campaign.getDm().getUsername().equals(userDetails.getUsername())) {
			campaignService.deleteCampaign(id);
			return "redirect:/campaign";
		} else {
			return "error/unauthorized";
		}
	}

	@GetMapping("/campaign/{id}/settings")
	public String editCampaignSettings(@PathVariable Long id, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		Campaign campaign = campaignService.getCampaignById(id);
		if (!campaign.getDm().getUsername().equals(userDetails.getUsername())) {
			return "errors/unauthorized";
		}
		List<User> users = campaignService.getAllPlayerById(id);
		model.addAttribute("campaign", campaign);
		model.addAttribute("users", users);
		model.addAttribute("dm", campaign.getDm());
		return "campaign_settings";
	}

	@PostMapping("/campaign/{id}/settings")
	public String updateCampaignSettings(@PathVariable Long id, @RequestParam String newCampaignName,
			@AuthenticationPrincipal UserDetails userDetails) {
		Campaign campaign = campaignService.getCampaignById(id);
		if (campaign.getDm().getUsername() != userDetails.getUsername()) {
			return "errors/unauthorized";
		}
		campaignService.updateCampaign(id, newCampaignName);
		return null;
	}
	
	@PostMapping("/campaign/sendCoins")
	public String showWallet(@RequestParam Long campaignId, @RequestParam Long fromWalletId, Model model) {
		Campaign campaign = campaignService.getCampaignById(campaignId);
		Wallet fromWallet = walletService.findById(fromWalletId);
		Set<User> users = campaign.getUsers();
		model.addAttribute("campaign", campaign);
		model.addAttribute("fromWallet", fromWallet);
		model.addAttribute("users", users);
		return "send_coins";
	}
	
	@PostMapping("/campaign/sendCoins/send")
	public String sendCoins(@RequestParam Long campaignId, @RequestParam Long fromWalletId,
			@RequestParam Long toWalletId, @RequestParam int copper, @RequestParam int silver, @RequestParam int gold,
			@RequestParam int platinum, Model model, @AuthenticationPrincipal UserDetails userDetails) {
		Wallet fromWallet = walletService.findById(fromWalletId);
		Wallet toWallet = walletService.findById(toWalletId);
		Campaign campaign = campaignService.getCampaignById(campaignId);
		try {
			walletService.sendCoins(fromWalletId, toWalletId, copper, silver, gold, platinum, userDetails);
			transactionService.saveTransaction(fromWallet, toWallet, copper, silver, gold, platinum, campaign);
			return "redirect:/campaign/" + campaignId;
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "redirect:/campaign/" + campaignId;
		}
	}

	@GetMapping("/getUserWallets")
	@ResponseBody
	public Map<String, Object> getUserWallets(@RequestParam String username, @RequestParam Long campaignId) {
		User user = userService.findByUsername(username);
		Campaign campaign = campaignService.getCampaignById(campaignId);
		List<Wallet> wallets = walletService.findByUserAndCampaign(user, campaign);
		Map<String, Object> response = new HashMap<>();
		response.put("wallets", wallets);

		return response;
	}
	
	@GetMapping("/getTransactions")
	public String getWalletTransactions(@RequestParam Long userId, @RequestParam Long campaignId, Model model) {
		User user = userService.findById(userId);
		Campaign campaign = campaignService.getCampaignById(campaignId);
		List<Transaction> transactions = transactionService.getTransactionsForUsers(user, campaign);
		model.addAttribute("transactions", transactions);
		return "wallet_transactions";
	}

	@GetMapping("/")
	public String redirectToCampaign() {
		return "redirect:/campaign";
	}
}
