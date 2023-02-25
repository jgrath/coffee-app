package com.app.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class Product {
    String productCode;
    private Integer productId;
    private double price;
    private boolean rewardItem;

    private boolean extraItem;

    private boolean foodItem;

    public Product(String productCode, Integer productId, double price, boolean rewardItem, boolean extraItem, boolean foodItem) {
        this.productCode = productCode;
        this.productId = productId;
        this.price = price;
        this.rewardItem = rewardItem;
        this.extraItem = extraItem;
        this.foodItem = foodItem;
    }

    public static Optional<Product> findProduct(String productCode, List<Product> listOfProductsToSearch, boolean promotionOnly) {

        Iterator<Product> productIterator = listOfProductsToSearch.iterator();
        while (productIterator.hasNext()) {
            Product product = productIterator.next();
            if (product.productCode.equals(productCode)) {
                return Optional.of(product);
            }
            if (promotionOnly) {
                if (product.productCode.equals(productCode) && product.isRewardItem() == true) {
                    return Optional.of(product);
                }
            }
        }
        return Optional.empty();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isRewardItem() {
        return rewardItem;
    }

    public void setRewardItem(boolean rewardItem) {
        this.rewardItem = rewardItem;
    }

    public boolean isExtraItem() {
        return extraItem;
    }

    public void setExtraItem(boolean extraItem) {
        this.extraItem = extraItem;
    }

    public boolean isFoodItem() {
        return foodItem;
    }

    public void setFoodItem(boolean foodItem) {
        this.foodItem = foodItem;
    }
}

