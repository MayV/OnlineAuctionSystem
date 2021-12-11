package com.mv.auctionSystem.services;

import com.mv.auctionSystem.enums.ObserverType;
import com.mv.auctionSystem.models.Event;
import com.mv.auctionSystem.models.Publisher;

import java.util.List;

public interface PublisherService {

    void updateObservers(List<String> observerIds, ObserverType type, Event event);

    boolean subscribe(String subjectId, String observerId, ObserverType type);

    boolean unsubscribe(String subjectId, String observerId, ObserverType type);
}
