package com.mv.auctionSystem.models;

import java.util.*;

import com.mv.auctionSystem.enums.ObserverType;

public abstract class Publisher {
	private Map<ObserverType, List<String>> observers;
	
	public Publisher() {
		this.observers = new HashMap<>();
		this.observers.put(ObserverType.SELLER, new LinkedList<>());
		this.observers.put(ObserverType.BUYER, new LinkedList<>());
	}
	
	public Map<ObserverType, List<String>> getObservers() {
		return this.observers;
	}
}
