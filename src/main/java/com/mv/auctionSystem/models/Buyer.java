package com.mv.auctionSystem.models;

import com.sun.istack.internal.NotNull;
import java.util.*;

public class Buyer extends User {
    private Map<String, Map<String, Double>> subscribedAuctions;

    public Buyer(@NotNull String BuyerId, @NotNull String username, @NotNull String emailId, @NotNull String phoneNo) {
        super(BuyerId, username,emailId, phoneNo);
        this.subscribedAuctions = new HashMap<>();
    }

    public Map<String, Map<String, Double>> getSubscribedAuctions() {
        return this.subscribedAuctions;
    }
}
