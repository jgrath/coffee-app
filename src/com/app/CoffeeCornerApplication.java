package com.app;

import com.app.domain.Basket;
import com.app.domain.BasketItem;
import com.app.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public class CoffeeCornerApplication {

    BasketCalculator basketCalculator;

    ReceiptPrinter receiptPrinter;

    List<Product> listOfAllAvailableProducts;

    public CoffeeCornerApplication(List<Product> listOfAllAvailableProducts) {
        this.basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        this.listOfAllAvailableProducts = listOfAllAvailableProducts;
        this.receiptPrinter = new ReceiptPrinter();
    }

    public double execute(List<BasketItem> basketItemList) {

        Basket basket = new Basket(basketItemList, 0);

        double basketTotal = basketCalculator.calculateBasket(basket);

        var basketLineItemsToPrint = basketItemList.stream()
                .map(item -> {
                    var product = Product.findProduct(item.getProductCode(), listOfAllAvailableProducts, false).get();
                    return item.getProductCode() + ", chf " + product.getPrice() + ", " + (item.isIncludeInPromotion() ? "Free" : "");
                })
                .collect(Collectors.toList());

        receiptPrinter.print(basketLineItemsToPrint, basketTotal);

        return basketTotal;
    }
}