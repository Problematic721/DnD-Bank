package com.example.dndbank.model;

import jakarta.persistence.*;

@Entity
@Table(name = "wallet")
public class Wallet {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private int copper;
	private int silver;
	private int gold;
	private int platinum;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
	
	public Wallet() {}
	
	public Wallet(String name, User user) {
		this.name = name;
        this.user = user;
	}
	
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

	public int getCopper() {
		return copper;
	}

	public void setCopper(int copper) {
		this.copper = copper;
	}

	public int getSilver() {
		return silver;
	}

	public void setSilver(int silver) {
		this.silver = silver;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getPlatinum() {
		return platinum;
	}

	public void setPlatinum(int platinum) {
		this.platinum = platinum;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
	
	
}
