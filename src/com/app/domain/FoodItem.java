package com.app.domain;


public class FoodItem extends Product {

    public FoodItem(String productCode, Integer priceId, double price) {
        super(productCode, priceId, price, false, false, true);
    }
}
