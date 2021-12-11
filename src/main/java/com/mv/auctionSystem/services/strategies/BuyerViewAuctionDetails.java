package com.mv.auctionSystem.services.strategies;

import com.mv.auctionSystem.enums.AuctionProductState;
import com.mv.auctionSystem.enums.AuctionState;
import com.mv.auctionSystem.models.Auction;
import com.mv.auctionSystem.models.AuctionProduct;
import com.mv.auctionSystem.models.Product;
import com.mv.auctionSystem.repository.AuctionRepository;
import com.mv.auctionSystem.repository.ProductRepository;

public class BuyerViewAuctionDetails extends AuctionDetails{
    AuctionRepository auctionRepository = AuctionRepository.getInstance();
    ProductRepository productRepository = ProductRepository.getInstance();

    public String getAuctionDetails(String buyerId, String auctionId) {
        Auction auction = auctionRepository.getData(auctionId);
        if(auction.getAuctionState()== AuctionState.STARTED) {
            StringBuilder data = new StringBuilder();
            data.append("Auction start Date & Time" + auction.getStartDate().toString() + "\n");
            data.append("Auction end Date & Time" + auction.getEndDate().toString() + "\n");
            for (AuctionProduct auctionProduct : auction.getProducts()) {
                Product product = productRepository.getData(auctionProduct.getProductId());
                data.append("Auction Product : " + product.getName() + ", " +
                        product.getDescription() + ", ");
                if (auctionProduct.getAuctionProductState() == AuctionProductState.SOLD) {
                    data.append("State: SOLD");
                } else {
                    data.append("State: UNSOLD" + ", ");
                    if (auctionProduct.getAuctionProductState() == AuctionProductState.UNBID) {
                        data.append("Bid starts at: " + "No bid placed till now");
                    } else {
                        data.append("Current Bid: " + auctionProduct.getCurrentBid());
                    }
                }
                data.append("\n");
            }
            return data.toString();
        }
        else {
            System.out.println("Error! Auction should not be visible to the user.");
            return null;
        }
    }
}
