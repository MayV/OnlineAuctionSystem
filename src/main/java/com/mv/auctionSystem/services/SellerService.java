package com.mv.auctionSystem.services;

import com.mv.auctionSystem.models.*;
import com.mv.auctionSystem.repository.SellerRepository;
import com.mv.auctionSystem.utils.Utils;
import com.sun.istack.internal.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SellerService implements ObserverService {
	SellerRepository sellerRepository = SellerRepository.getInstance();
	AuctionService auctionService = new AuctionService();

	private String generateSellerId() {
		return UUID.randomUUID().toString()+".SI";
	}

	public void addSeller(@NotNull String username, @NotNull String emailId, @NotNull String phoneNo) {
		boolean validEmailId = Utils.validateEmailId(emailId);
		if(!validEmailId) {
			System.out.println("Please enter valid emailId.");
			return;
		}
		boolean validPhoneNo = Utils.validatePhoneNo(phoneNo);
		if(!validPhoneNo) {
			System.out.println("Please enter valid phoneNo.");
			return;
		}
		// TODO - Check username is not already used. If used, do not create new seller and return.
		String sellerId = this.generateSellerId();
		Seller seller = new Seller(sellerId, username, emailId, phoneNo);
		sellerRepository.addDataToRepository(sellerId, seller);
	}

	public List<String> viewAllSellerOwnedAuctions(String sellerId) {
		boolean validate = this.validateSeller(sellerId);
		if(!validate) {
			System.out.println("Seller Id not valid");
			return null;
		}
		Seller seller = sellerRepository.getData(sellerId);
		List<String> response = new LinkedList<>();
		for(String auctionId : seller.getAuctions()) {
			String auctionDetails = auctionService.getAuctionDetails(sellerId, auctionId, "SellerView");
			if(auctionDetails==null) {
				continue;
			}
			response.add(auctionDetails);
		}
		return response;
	}

	private static final int MAX_NOTIFICATION_COUNT = 10; //Maximum no. of notifications to return

	public List<String> getNotifications(String sellerId) {
		boolean validate = this.validateSeller(sellerId);
		if(!validate) {
			System.out.println("Seller Id not valid");
			return null;
		}
		Seller seller = sellerRepository.getData(sellerId);
		List<String> retval = new LinkedList<>();
		for(Notification notification : seller.getNotifications()) {
			retval.add(notification.toString());
			if(retval.size() == MAX_NOTIFICATION_COUNT) {
				break;
			}
		}
		return retval;
	}

	public boolean validateSeller(String sellerId) {
		return sellerRepository.getData(sellerId) != null ? true : false;
	}

	@Override
	//This method will be called by the publisher to update the observer.
	public void updateObserver(List<String> sellerIds, Event event) {
		for(String sellerId : sellerIds) {
			boolean validate = this.validateSeller(sellerId);
			if(!validate) {
				continue;
			}
			Seller seller = sellerRepository.getData(sellerId);
			Notification notification = new Notification(event.getMessage(), event.getDateTime());
			seller.getNotifications().add(0, notification);
		}
	}
	
}
