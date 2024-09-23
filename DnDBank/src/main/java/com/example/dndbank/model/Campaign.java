package com.example.dndbank.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "campaign")
public class Campaign {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true)
	private String joinCode;

	@OneToMany(mappedBy = "campaign", cascade = CascadeType.REMOVE)
	private Set<Wallet> wallets = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "dm_id", nullable = false)
	private User dm;

	@ManyToMany
	@JoinTable(name = "user_campaigns", joinColumns = @JoinColumn(name = "campaign_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Transaction> transactions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Wallet> getWallets() {
		return wallets;
	}

	public void setWallets(Set<Wallet> wallets) {
		this.wallets = wallets;
	}

	public String getJoinCode() {
		return joinCode;
	}

	public void setJoinCode(String joinCode) {
		this.joinCode = joinCode;
	}

	public User getDm() {
		return dm;
	}

	public void setDm(User dm) {
		this.dm = dm;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
}
