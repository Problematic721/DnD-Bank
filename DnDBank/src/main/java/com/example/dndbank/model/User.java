package com.example.dndbank.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "player")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    
    @Transient
    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Wallet> wallets;
	
    @ManyToMany
    @JoinTable(name = "user_campaigns",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "campaign_id"))
    private Set<Campaign> campaigns = new HashSet<>();
    
    @ManyToMany(mappedBy = "dm")
    private Set<Campaign> dmCampaigns = new HashSet<>();
    
	public User() {
		
	}
	
	public User(String username, String password, String email) {
		setUsername(username);
		setPassword(password);
		setEmail(email);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public boolean passwordsMatch() {
        return this.password.equals(this.confirmPassword);
    }
	
	public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public void addWallet(Wallet wallet) {
        this.wallets.add(wallet);
        wallet.setUser(this);
    }

	public Set<Campaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(Set<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public Set<Campaign> getDmCampaigns() {
		return dmCampaigns;
	}

	public void setDmCampaigns(Set<Campaign> dmCampaigns) {
		this.dmCampaigns = dmCampaigns;
	}
    
}
