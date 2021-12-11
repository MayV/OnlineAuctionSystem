package com.mv.auctionSystem.models;

import com.mv.auctionSystem.enums.AuctionProductState;

import java.util.Objects;

public class AuctionProduct {
	String productId;
	Double basePrice;
	AuctionProductState auctionProductState;
	private Double currentBid;
	private String currentWinningBuyerId;

	public AuctionProduct(String productId, double basePrice) {
		this.productId = productId;
		this.basePrice = basePrice;
		this.auctionProductState = AuctionProductState.UNBID;
		this.currentBid = 0.0;
		this.currentWinningBuyerId = null;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public AuctionProductState getAuctionProductState() {
		return auctionProductState;
	}

	public void setAuctionProductState(AuctionProductState auctionProductState) {
		this.auctionProductState = auctionProductState;
	}

	public Double getCurrentBid() {
		return currentBid;
	}

	public void setCurrentBid(Double currentBid) {
		this.currentBid = currentBid;
	}

	public String getCurrentWinningBuyerId() {
		return currentWinningBuyerId;
	}

	public void setCurrentWinningBuyerId(String currentWinningBuyerId) {
		this.currentWinningBuyerId = currentWinningBuyerId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getProductId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AuctionProduct)) return false;
		AuctionProduct that = (AuctionProduct) o;
		return Objects.equals(this.getProductId(), that.getProductId());
	}
}
