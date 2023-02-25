package com.app.domain;

import java.util.List;

public class Basket {

    private List<BasketItem> basketItems;

    private int numberOfFreeBeverages;

    public Basket(List<BasketItem> basketItems, int numberOfFreeBeverages) {
        this.basketItems = basketItems;
        this.numberOfFreeBeverages = numberOfFreeBeverages;
    }

    public List<BasketItem> getBasketItems() {
        return basketItems;
    }

    public void setBasketItems(List<BasketItem> basketItems) {
        this.basketItems = basketItems;
    }

    public int getNumberOfFreeBeverages() {
        return numberOfFreeBeverages;
    }

    public void setNumberOfFreeBeverages(int numberOfFreeBeverages) {
        this.numberOfFreeBeverages = numberOfFreeBeverages;
    }
}

