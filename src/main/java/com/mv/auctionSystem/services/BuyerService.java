package com.mv.auctionSystem.services;

import com.mv.auctionSystem.enums.ObserverType;
import com.mv.auctionSystem.models.*;
import com.mv.auctionSystem.repository.BuyerRepository;
import com.mv.auctionSystem.utils.Utils;
import com.sun.istack.internal.NotNull;

import java.util.*;

public class BuyerService implements ObserverService {
    private final BuyerRepository buyerRepository = BuyerRepository.getInstance();
    private final AuctionService auctionService = new AuctionService();
    private static final ObserverType BUYER_AS_OBSERVER = ObserverType.BUYER;

    private String generateBuyerId() {
        return UUID.randomUUID().toString()+".BI";
    }

    public void addBuyer(@NotNull String username, @NotNull String emailId, @NotNull String phoneNo) {
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
        // TODO - Check username is not already used. If used, do not create new buyer and return.
        String buyerId = this.generateBuyerId();
        Buyer buyer = new Buyer(buyerId, username, emailId, phoneNo);
        buyerRepository.addDataToRepository(buyerId, buyer);
    }

    public List<String> viewAllBuyerBids(String buyerId) {
        boolean validate = this.validateBuyer(buyerId);
        if(!validate) {
            System.out.println("Buyer Id not valid");
            return null;
        }
        Buyer buyer = buyerRepository.getData(buyerId);
        List<String> response = new LinkedList<>();
        Map<String, Map<String, Double>> subscribedAuctions = buyer.getSubscribedAuctions();
        for(String auctionId : subscribedAuctions.keySet()) {
            for(String productId : subscribedAuctions.get(auctionId).keySet()) {
                String data = "AuctiondId: " + auctionId + ", "
                        + "ProductId: " + productId + ", "
                        + "BidValue: " + subscribedAuctions.get(auctionId).get(productId);
                response.add(data);
            }
        }
        return response;
    }

    public void subscribeToAuction(String buyerId, String auctionId) {
        boolean validate = this.validateBuyer(buyerId);
        if(!validate) {
            System.out.println("Buyer Id not valid");
            return;
        }
        Buyer buyer = buyerRepository.getData(buyerId);
        if(buyer.getSubscribedAuctions().containsKey(auctionId)) {
            System.out.println("Already subscribed to the auction.");
            return;
        }
        boolean success = auctionService.subscribe(auctionId, buyerId, BUYER_AS_OBSERVER);
        if(success) {
            buyer.getSubscribedAuctions().put(auctionId, new HashMap<>());
        }
        else {
            System.out.println("Failed to subscribe.");
        }
    }

    public void unsubscribeToAuction(String buyerId, String auctionId) {
        boolean validate = this.validateBuyer(buyerId);
        if(!validate) {
            System.out.println("Buyer Id not valid");
            return;
        }
        Buyer buyer = buyerRepository.getData(buyerId);
        if(!buyer.getSubscribedAuctions().containsKey(auctionId)) {
            System.out.println("Have not subscribed to the auction.");
            return;
        }
        boolean success = auctionService.unsubscribe(auctionId, buyerId, BUYER_AS_OBSERVER);
        if(success) {
            buyer.getSubscribedAuctions().remove(auctionId);
        }
        else {
            System.out.println("Failed to unsubscribe.");
        }
    }

    //Access only from AuctionService
    public boolean placeBid(String buyerId, String auctionId, String productId, double bidValue) {
        boolean validate = this.validateBuyer(buyerId);
        if(!validate) {
            System.out.println("Buyer Id not valid");
            return false;
        }
        subscribeToAuction(buyerId, auctionId);
        Buyer buyer = buyerRepository.getData(buyerId);
        Map<String, Map<String, Double>> subscribedAuctions = buyer.getSubscribedAuctions();
        if(subscribedAuctions.containsKey(auctionId)) {
            Map<String, Double> existingBids = subscribedAuctions.get(auctionId);
            if(existingBids.containsKey(productId)) {
                double exisitingBidValue = existingBids.get(productId);
                if(exisitingBidValue <= bidValue) {
                    System.out.println("You have already bid with less/same amount. Increase bid.");
                    return false;
                }
            }
            existingBids.put(productId, bidValue);
        }
        else {
            System.out.println("subscribeToAuction call failed.");
            return false;
        }
        return true;
    }

    private static final int MAX_NOTIFICATION_COUNT = 10; //Maximum no. of notifications to return

    public List<String> getNotifications(String buyerId) {
        boolean validate = this.validateBuyer(buyerId);
        if(!validate) {
            System.out.println("Buyer Id not valid");
            return null;
        }
        Buyer buyer = buyerRepository.getData(buyerId);
        List<String> retval = new LinkedList<>();
        for(Notification notification : buyer.getNotifications()) {
            retval.add(notification.toString());
            if(retval.size() == MAX_NOTIFICATION_COUNT) {
                break;
            }
        }
        return retval;
    }

    public boolean validateBuyer(String buyerId) {
        return buyerRepository.getData(buyerId) != null ? true : false;
    }

    @Override
    public void updateObserver(List<String> buyerIds, Event event) {
        for(String buyerId : buyerIds) {
            boolean validate = this.validateBuyer(buyerId);
            if(!validate) {
                continue;
            }
            Buyer buyer = buyerRepository.getData(buyerId);
            Notification notification = new Notification(event.getMessage(), event.getDateTime());
            buyer.getNotifications().add(0, notification);
        }
    }

}
