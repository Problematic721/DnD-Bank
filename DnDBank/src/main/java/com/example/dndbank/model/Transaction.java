package com.example.dndbank.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_wallet_id")
    private Wallet fromWallet;

    @ManyToOne
    @JoinColumn(name = "to_wallet_id")
    private Wallet toWallet;

    private Integer copper;
    private Integer silver;
    private Integer gold;
    private Integer platinum;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(nullable = false)
    private LocalDateTime transactionDate;
    
    public Transaction() {}

	public Transaction(Wallet fromWallet, Wallet toWallet, int copper, int silver, int gold, int platinum, Campaign campaign) {
		this.fromWallet = fromWallet;
        this.toWallet = toWallet;
        this.copper = copper;
        this.silver = silver;
        this.gold = gold;
        this.platinum = platinum;
        this.campaign = campaign;
        this.setTransactionDate(LocalDateTime.now());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Wallet getFromWallet() {
		return fromWallet;
	}

	public void setFromWallet(Wallet fromWallet) {
		this.fromWallet = fromWallet;
	}

	public Wallet getToWallet() {
		return toWallet;
	}

	public void setToWallet(Wallet toWallet) {
		this.toWallet = toWallet;
	}

	public Integer getCopper() {
		return copper;
	}

	public void setCopper(Integer copper) {
		this.copper = copper;
	}

	public Integer getSilver() {
		return silver;
	}

	public void setSilver(Integer silver) {
		this.silver = silver;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getPlatinum() {
		return platinum;
	}

	public void setPlatinum(Integer platinum) {
		this.platinum = platinum;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

    
}