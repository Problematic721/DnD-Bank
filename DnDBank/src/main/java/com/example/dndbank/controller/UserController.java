package com.example.dndbank.controller;

import com.example.dndbank.model.User;
import com.example.dndbank.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class UserController {
	
	@Autowired
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String showLoginPage(@RequestParam (required = false) String error, Model model) {
		 if (error != null) {
		        model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
		    }
		return "login";
	}

	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@GetMapping("/registration_complete")
	public String showRegistrationCompletePage() {
		return "register_complete";
	}

	@GetMapping("/resetPassword")
	public String showResetPasswordPage() {
		return "reset";
	}

	@PostMapping("/register")
	public String processRegistration(@Valid @ModelAttribute User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "register";
		}
		if (!user.passwordsMatch()) {
			result.rejectValue("confirmPassword", "error.user", "Passwords do not match");
			return "register";
		}
		userService.registerUser(user);
		return "redirect:/login";
	}

	@PostMapping("/reset_password")
	public String handleResetPassword(@RequestParam String email, @RequestParam String newPassword, Model model) {
		boolean isReset = userService.resetPassword(email, newPassword);
		if (isReset) {
			return "redirect:/login";
		} else {
			return "reset_password";
		}
	}
}