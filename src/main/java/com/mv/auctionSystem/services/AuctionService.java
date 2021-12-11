package com.mv.auctionSystem.services;

import com.mv.auctionSystem.enums.AuctionProductState;
import com.mv.auctionSystem.enums.AuctionState;
import com.mv.auctionSystem.enums.ObserverType;
import com.mv.auctionSystem.enums.PublisherEventType;
import com.mv.auctionSystem.models.*;
import com.mv.auctionSystem.repository.AuctionRepository;
import com.mv.auctionSystem.repository.ProductRepository;
import com.mv.auctionSystem.services.strategies.AuctionDetails;
import com.mv.auctionSystem.services.strategies.BuyerViewAuctionDetails;
import com.mv.auctionSystem.services.strategies.SellerViewAuctionDetails;

import java.util.*;

public class AuctionService implements PublisherService{
	private final ProductRepository productRepository = ProductRepository.getInstance();
	private final AuctionRepository auctionRepository = AuctionRepository.getInstance();
	private final BuyerService buyerService = new BuyerService();
	private final SellerService sellerService = new SellerService();

	private String generateAuctionId() {
		return UUID.randomUUID().toString()+".AI";
	}

	private String generateProductId() {
		return UUID.randomUUID().toString()+".PI";
	}

	private boolean validateSeller(String sellerId) {
		return sellerService.validateSeller(sellerId);
	}

	private boolean validateBuyer(String buyerId) {
		return buyerService.validateBuyer(buyerId);
	}

	public void draftAuction(String sellerId) {
		boolean validate = this.validateSeller(sellerId);
		if(!validate) {
			System.out.println("Seller Id is invalid");
			return;
		}
		Auction auction = new Auction(this.generateAuctionId());
		auctionRepository.addDataToRepository(auction.getAuctionId(), auction);
		System.out.println("Auction created. Auction is in DRAFT state.");
	}

	public void updateAuctionProduct(String sellerId, String auctionId, String productName, String productDescription, double basePrice) {
		boolean validate = this.validateSeller(sellerId);
		if(!validate) {
			System.out.println("Seller Id is invalid");
			return;
		}
		validate = this.validateAuction(auctionId);
		if(!validate) {
			System.out.println("Auction Id is invalid");
			return;
		}
		Auction auction = auctionRepository.getData(auctionId);
		if(auction.getAuctionState() != AuctionState.DRAFTED) {
			System.out.println("Auction is not in draft state. Making any change is not allowed.");
			return;
		}
		Product product = new Product(this.generateProductId(), productName, productDescription, auctionId);
		AuctionProduct auctionProduct = new AuctionProduct(product.getProductId(), basePrice);
		auction.setProducts(Arrays.asList(new AuctionProduct[]{auctionProduct}));
		System.out.println("Product Added");
	}

	// TODO - Add functions to update auction details, product details & auction base price.

	private final long MAX_START_TIME_DELAY = 3*3600;
	private final long MIN_AUCTION_LIFE = 3600;
	private final long MAX_AUCTION_LIFE = 3*3600;

	public void publishAuction(String sellerId, String auctionId, Date startTime, Date endTime) {
		boolean validate = this.validateSeller(sellerId);
		if(!validate) {
			System.out.println("Seller Id is invalid");
			return;
		}
		validate = this.validateAuction(auctionId);
		if(!validate) {
			System.out.println("Auction Id is invalid");
			return;
		}
		Auction auction = auctionRepository.getData(auctionId);
		if(auction.getProducts() == null || auction.getProducts().size() == 0) {
			System.out.println("Add a product before publishing auction.");
			return;
		}
		if(auction.getAuctionState() != AuctionState.DRAFTED) {
			System.out.println("Auction is not in draft state. Make changes is not allowed.");
			return;
		}
		Date currentTime = new Date();
		if(startTime==null) {
			startTime = currentTime;
		}
		if(endTime==null) {
			endTime = new Date(startTime.getTime() + MIN_AUCTION_LIFE);
		}
		if(endTime.compareTo(currentTime) < 0 || startTime.compareTo(currentTime) < 0) {
			System.out.println("Invalid start date or end date. Start Date and End Date should be greater than current Date");
			return;
		}
		else if(startTime.getTime()-currentTime.getTime() > MAX_START_TIME_DELAY) {
			System.out.println("Start Date should be less than current Date + 3 hours");
			return;
		}
		else if(endTime.getTime()-startTime.getTime() > MAX_AUCTION_LIFE) {
			System.out.println("Auction cannot be organised for more than allowed time.");
			return;
		}
		else if(endTime.getTime()-startTime.getTime() < MIN_AUCTION_LIFE) {
			System.out.println("Auction cannot be organised for less than threshold time.");
			return;
		}
		auction.setAuctionState(AuctionState.PUBLISHED);
		if(currentTime==startTime) {
			startAuction(auctionId);
		}
		else {
			//TODO - Write logic
			/*
			Schedule auction in scheduler service.
			Schedule the start and end of auction. For that we can have a Scheduler Service.
			Scheduler Service will call the startAuction() api of AuctionService to start the auction when is it time.
			Scheduler Service will call the endAuction() api of AuctionService to end the auction when it is time.
			*/
		}

	}

	//Will be called by the Scheduler Service.
	public void startAuction(String auctionId) {
		boolean validate = this.validateAuction(auctionId);
		if(!validate) {
			System.out.println("Auction Id is invalid");
			return;
		}
		Auction auction = auctionRepository.getData(auctionId);
		auction.setAuctionState(AuctionState.STARTED);
		updateObserversAboutAuctionStarted(auction);
	}

	//Will be called by the Scheduler Service.
	public void endAuction(String auctionId) {
		boolean validate = this.validateAuction(auctionId);
		if(!validate) {
			System.out.println("Auction Id is invalid");
			return;
		}
		Auction auction = auctionRepository.getData(auctionId);
		for(AuctionProduct auctionProduct : auction.getProducts()) {
			if(auctionProduct.getAuctionProductState().equals(AuctionProductState.UNBID)) {
				auctionProduct.setAuctionProductState(AuctionProductState.FAILED);
				auction.setAuctionState(AuctionState.END_FAILED);
			}
			else if(auctionProduct.getCurrentBid() >= auctionProduct.getBasePrice()) {
				auctionProduct.setAuctionProductState(AuctionProductState.SOLD);
			}
			else {
				auctionProduct.setAuctionProductState(AuctionProductState.FAILED);
				auction.setAuctionState(AuctionState.END_FAILED);
			}
		}
		if(!auction.getAuctionState().equals(AuctionState.END_FAILED)) {
			auction.setAuctionState(AuctionState.END_SUCCESS);
		}
		updateObserversAboutAuctionEnded(auction);
	}

	public void placeBid(String auctionId, String productId, String buyerId, double bidValue) {
		boolean validate = this.validateAuction(auctionId);
		if(!validate) {
			System.out.println("Auction Id is invalid");
			return;
		}
		validate = this.validateBuyer(buyerId);
		if(!validate) {
			System.out.println("Buyer Id is invalid");
			return;
		}
		Auction auction = auctionRepository.getData(auctionId);
		AuctionProduct auctionProduct = auction.getProducts().stream().filter(ap -> ap.getProductId().equals(productId)).findFirst().orElse(null);
		if(auctionProduct == null) {
			System.out.println("productId given does not belong to this auction.");
			return;
		}
		if(auction.getAuctionState() == AuctionState.STARTED &&
				auctionProduct.getAuctionProductState() != AuctionProductState.SOLD &&
				auctionProduct.getAuctionProductState() != AuctionProductState.FAILED) {
			if(bidValue < auctionProduct.getCurrentBid()) {
				System.out.println("Bid Value should be greater than current bid price.");
				return;
			}
			if(auctionProduct.getAuctionProductState() == AuctionProductState.UNBID) {
				auctionProduct.setAuctionProductState(AuctionProductState.UNSOLD);
			}
			buyerService.placeBid(buyerId, auctionId, productId, bidValue);
			auctionProduct.setCurrentBid(bidValue);
			auctionProduct.setCurrentWinningBuyerId(buyerId);
			auctionProduct.setAuctionProductState(AuctionProductState.UNSOLD);
			updateObserverAboutNewBidPrice(auction, bidValue);
		}
		else {
			System.out.println("Either auction is not started or auction is ended or product is sold.");
		}
	}

	@Override
	// Will be called by the ObserverService only.
	public boolean subscribe(String auctionId, String observerId, ObserverType type) {
		boolean validate = this.validateAuction(auctionId);
		if(!validate) {
			System.out.println("Auction Id is invalid");
			return false;
		}
		Auction auction = auctionRepository.getData(auctionId);
		auction.getObservers().get(type).add(observerId);
		return true;
	}

	@Override
	//Will be called by the ObserverService only.
	public boolean unsubscribe(String auctionId, String observerId, ObserverType type) {
		boolean validate = this.validateAuction(auctionId);
		if(!validate) {
			System.out.println("Auction Id is invalid");
			return false;
		}
		if(ObserverType.SELLER == type) {
			System.out.println("Seller not allowed to unsubscribe his auction");
			return false;
		}
		Auction auction = auctionRepository.getData(auctionId);
		//If observer is a Buyer, check if current observers is not the current winning Buyer
		if(ObserverType.BUYER == type) {
			boolean eligible = eligibleToUnsubscribe(auction, observerId);
			if (!eligible) {
				return false;
			}
		}
		auction.getObservers().get(type).remove(observerId);
		return true;
	}

	private boolean eligibleToUnsubscribe(Auction auction, String observerId) {
		List<AuctionProduct> auctionProductList = auction.getProducts();
		for(AuctionProduct auctionProduct : auctionProductList) {
			if(auctionProduct.getCurrentWinningBuyerId().equals(observerId)) {
				System.out.println("Buyer is current winning bidder for one of the product and so not allowed to unsubscribe.");
				return false;
			}
		}
		return true;
	}

	private void updateObserversAboutAuctionStarted(Auction auction) {
		Event event = new Event();
		event.setEventType(PublisherEventType.AUCTION_STARTED);
		event.setMessage("Auction Started! - " + auction.getAuctionId());
		event.setDateTime(new Date());

		updateObservers(auction, ObserverType.BOTH, event);
	}

	private void updateObserversAboutAuctionEnded(Auction auction) {
		Event event;

		event = new Event();
		event.setEventType(PublisherEventType.AUCTION_ENDED);
		event.setMessage("Auction Ended! - " + auction.getAuctionId());
		event.setDateTime(new Date());

		updateObservers(auction, ObserverType.BOTH, event);

		for(AuctionProduct auctionProduct : auction.getProducts()) {
			if(auctionProduct.getAuctionProductState().equals(AuctionProductState.FAILED)) {
				continue;
			}
			event = new Event();
			event.setEventType(PublisherEventType.AUCTION_ENDED);
			event.setMessage("Auction Ended! - " + auction.getAuctionId()
					+ "Congrats! You bought the product - " + auctionProduct.getProductId());
			event.setDateTime(new Date());
			updateObservers(Arrays.asList(auctionProduct.getCurrentWinningBuyerId()), ObserverType.BUYER, event);
		}
	}

	private void updateObserverAboutNewBidPrice(Auction auction, double bidValue) {
		Event event = new Event();
		event.setEventType(PublisherEventType.BID_VALUE_UPDATED);
		event.setMessage("Bid Price updated! Auction - " + auction.getAuctionId() + "New Bid price is: " + bidValue);
		event.setDateTime(new Date());

		updateObservers(auction, ObserverType.BUYER, event);
	}

	private void updateObservers(Publisher auction, ObserverType observerType, Event event) {
		this.updateObservers(auction.getObservers().get(observerType), observerType, event);
	}

	@Override
	//Will be called by AuctionSerice class only when the state of auction changes.
	public void updateObservers(List<String> observerIds, ObserverType observerType, Event event) {
		ObserverService observerService;
		switch (observerType) {
			case BUYER:
				buyerService.updateObserver(observerIds, event);
				break;
			case SELLER:
				sellerService.updateObserver(observerIds, event);
				break;
			case BOTH:
				buyerService.updateObserver(observerIds, event);
				sellerService.updateObserver(observerIds, event);
				break;
			default:
				System.out.println("Observer Type not listed");
		}
	}

	public String getAuctionDetails(String userId, String auctionId, String algorithm) {
		AuctionDetails auctionDetailsObject = null;
		if(algorithm.equals("SellerView")) {
			auctionDetailsObject = new SellerViewAuctionDetails();
		}
		else {
			auctionDetailsObject = new BuyerViewAuctionDetails();
		}
		// Using Strategy Pattern
		String response = auctionDetailsObject.getAuctionDetails(userId, auctionId);
		return response;
	}

	public boolean validateAuction(String auctionId) {
		return auctionRepository.getData(auctionId) != null ? true : false;
	}
}
