package com.mv.auctionSystem.services;

import com.mv.auctionSystem.models.Event;

import java.util.List;

public interface ObserverService {

    void updateObserver(List<String> ids, Event event);

}
