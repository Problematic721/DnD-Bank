package com.example.dndbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dndbank.model.User;
import com.example.dndbank.service.UserService;

@Controller
public class AccountController {
	@Autowired
	private UserService userService;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/account")
	public String showAccountPage(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("activePage", "account");
        User user = userService.findByUsername(currentUser.getUsername());
        model.addAttribute("user", user);
        return "account";
    }
	
	@PostMapping("/account/update")
	public String updateAccount(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
		userService.updateUser(user);
		redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully!");
		return "redirect:/account";
	}
	
	@PostMapping("/account/updatePassword")
	public String updatePassword(@RequestParam String newPassword, @RequestParam String currentPassword, @AuthenticationPrincipal UserDetails currentUser,
            RedirectAttributes redirectAttributes) {
		User user = userService.findByUsername(currentUser.getUsername());
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
	        redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
	        return "redirect:/account/settings";
	    }
		userService.updatePassword(user, newPassword);
		redirectAttributes.addFlashAttribute("message", "Password updated successfully");
		return "redirect:/account";
	}
}
