package com.example.dndbank.service;

import com.example.dndbank.model.User;
import com.example.dndbank.model.Wallet;
import com.example.dndbank.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public boolean authenticate(String username, String password) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			return passwordEncoder.matches(password, user.getPassword());
		}

		return false;
	}

	public boolean registerUser(User user) {
		if (userRepository.findByUsername(user.getUsername()).isPresent()
				|| userRepository.findByEmail(user.getEmail()).isPresent()) {
			return false;
		}
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
		return true;
	}

	public boolean resetPassword(String email, String newPassword) {
		Optional<User> userOptional = userRepository.findByEmail(email);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return true;
		}
		return false;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(),
						user.getPassword(), new ArrayList<>()))
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public ArrayList<User> findAllUsers() {
		return userRepository.findAll();
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public User findById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public void updateUser(User user) {
		User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new UsernameNotFoundException(""));
		existingUser.setUsername(user.getUsername());
		existingUser.setEmail(user.getEmail());
		userRepository.save(existingUser);
	}

	public void updatePassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
	}
}
