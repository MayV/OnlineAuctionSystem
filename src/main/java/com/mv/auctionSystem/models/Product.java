package com.mv.auctionSystem.models;

import com.sun.istack.internal.NotNull;

import java.util.UUID;

public class Product {
	private String productId;
	private String name;
	private String description;
	private String auctionId;

	public Product(){}

	public Product(@NotNull String productId, @NotNull String name, String description, String auctionId) {
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.auctionId = auctionId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

}
