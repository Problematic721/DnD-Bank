package com.example.dndbank.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.dndbank.model.User;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
	private String username;
    private String password;
    // Add other fields as necessary

    // Constructor
    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        // Initialize other fields if needed
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Implement this if you have roles/authorities
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}