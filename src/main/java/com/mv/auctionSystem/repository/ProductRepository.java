package com.mv.auctionSystem.repository;

import com.mv.auctionSystem.models.Product;
import com.mv.auctionSystem.models.Seller;

import java.util.HashMap;
import java.util.Map;

public class ProductRepository {
	public static Map<String, Product> products;

	// TODO - Implement thread safe lazy initialization singleton pattern.
	private ProductRepository() {
		this.products = new HashMap<>();
	}
	private static ProductRepository single_instance = null;
	public static ProductRepository getInstance() {
		if(single_instance==null) {
			single_instance = new ProductRepository();
		}
		return single_instance;
	}

	public void addDataToRepository(String key, Product value) {
		this.products.put(key, value);
	}

	public Product getData(String key) {
		if(key==null) {
			return null;
		}
		return this.products.get(key);
	}
}
