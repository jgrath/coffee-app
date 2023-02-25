package com.app.domain;


public class RewardItem extends Product {

    public RewardItem(String productCode, Integer priceId, double price) {
        super(productCode, priceId, price, true, false, false);
    }

}