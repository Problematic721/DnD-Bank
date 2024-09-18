package com.example.dndbank.controller;

import com.example.dndbank.model.User;
import com.example.dndbank.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	@GetMapping("/login")
	public String showLoginPage() {
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

	@GetMapping("/reset_password")
	public String showResetPasswordPage() {
		return "reset";
	}

	@PostMapping("/login")
	public String handleLogin(@RequestParam String username, @RequestParam String password, Model model) {
		boolean isAuthenticated = userService.authenticate(username, password);
		if (isAuthenticated) {
			return "redirect:/campaign";
		} else {
			model.addAttribute("error", "Invalid username or password");
			return "login";
		}
	}

	@PostMapping("/register")
	public String processRegistration(@Valid User user, BindingResult result) {
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
	public String handleResetPassword(@RequestParam String email,@RequestParam String newPassword, Model model) {
		boolean isReset = userService.resetPassword(email, newPassword);
		if (isReset) {
			return "redirect:/login";
		} else {
			model.addAttribute("error", "Password reset failed");
			return "reset_password";
		}
	}
}