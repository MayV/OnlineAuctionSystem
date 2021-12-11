package com.mv.auctionSystem.models;

import com.mv.auctionSystem.enums.AuctionState;
import com.sun.istack.internal.NotNull;

import java.util.Date;
import java.util.List;

public class Auction extends Publisher{
	private String auctionId;
	private String sellerId;
	private List<AuctionProduct> products;	//Singleton List
	private AuctionState auctionState;
	private Date startDate;
	private Date endDate;
	
	public Auction(@NotNull String auctionId) {
		super();
		this.auctionId = auctionId;
		this.auctionState = AuctionState.DRAFTED;
	}

	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public AuctionState getAuctionState() {
		return auctionState;
	}

	public void setAuctionState(AuctionState auctionState) {
		this.auctionState = auctionState;
	}

	public List<AuctionProduct> getProducts() {
		return products;
	}

	public void setProducts(List<AuctionProduct> products) {
		this.products = products;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null) {
			return false;
		}
		if(obj instanceof Auction) {
			return this.getAuctionId().equals(((Auction) obj).getAuctionId());
		}
		return false;
	}
}
