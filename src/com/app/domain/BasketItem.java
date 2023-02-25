package com.app.domain;

import java.util.Optional;

public class BasketItem {

    private String productCode;

    private int units;

    private Optional<Integer> promotionsUnits = Optional.empty();

    private boolean includeInPromotion;

    public BasketItem(String productCode, int units) {
        this.productCode = productCode;
        this.units = units;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public Optional<Integer> getPromotionsUnits() {
        return promotionsUnits;
    }

    public void setPromotionsUnits(Optional<Integer> promotionsUnits) {
        this.promotionsUnits = promotionsUnits;
    }

    public boolean isIncludeInPromotion() {
        return includeInPromotion;
    }

    public void setIncludeInPromotion(boolean includeInPromotion) {
        this.includeInPromotion = includeInPromotion;
    }
}
