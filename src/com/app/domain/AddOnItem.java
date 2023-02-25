package com.app.domain;



public class AddOnItem extends Product {
    public AddOnItem(String productCode, Integer priceId, double price) {
        super(productCode, priceId, price, false, true, false);
    }
}