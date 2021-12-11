package com.mv.auctionSystem.models;

import com.sun.istack.internal.NotNull;

import java.util.List;

public class Seller extends User {
	private List<String> auctions; //List of AuctionId

	public Seller(@NotNull String sellerId, @NotNull String username, @NotNull String emailId, @NotNull String phoneNo) {
		super(sellerId, username,emailId, phoneNo);
	}

	public List<String> getAuctions() {
		return auctions;
	}
}
