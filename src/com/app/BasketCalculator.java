package com.app;

import com.app.domain.Basket;
import com.app.domain.Product;
import com.app.domain.PromotionResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class BasketCalculator {

    private List<Product> listOfProducts;

    public BasketCalculator(List<Product> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public double calculateBasket(Basket basket) {

        List<Product> shoppingBasketMatchedProducts = basket.getBasketItems().stream()
                .map(basketItem -> Product.findProduct(basketItem.getProductCode(), listOfProducts, false))
                .filter(Optional::isPresent)
                .map(x -> x.get())
                .collect(Collectors.toList());

        PromotionResult stampsAwarded = stampsAwarded(shoppingBasketMatchedProducts, basket);

        applyPromotions(shoppingBasketMatchedProducts, stampsAwarded, basket);

        return calculateBasketTotal(shoppingBasketMatchedProducts, basket);

    }

    public void applyPromotions(List<Product> shoppingBasketMatchedProducts, PromotionResult stampsAwarded, Basket basket) {

        List<Product> rewardsItemsInBasket = shoppingBasketMatchedProducts.stream()
                .filter(x -> x.isRewardItem()).collect(Collectors.toList());

        var numberOfFreeBeverages = stampsAwarded.stampsAwarded() / 5;

        if (numberOfFreeBeverages >= 1) {

            basket.getBasketItems().stream()
                    .map(basketItem -> {
                        basket.setNumberOfFreeBeverages(numberOfFreeBeverages);
                        return basketItem;
                    }).collect(Collectors.toList());
        }

        if (!rewardsItemsInBasket.isEmpty()) {

            List<Product> extrasItemsInBasket = shoppingBasketMatchedProducts.stream()
                    .filter(x -> x.isExtraItem()).collect(Collectors.toList());

            var extraItems = shoppingBasketMatchedProducts.stream()
                    .filter(x -> x.isExtraItem()).collect(Collectors.toList());
            var foodItems = shoppingBasketMatchedProducts.stream()
                    .filter(x -> x.isFoodItem()).collect(Collectors.toList());

            if (!extrasItemsInBasket.isEmpty() && !extraItems.isEmpty() && !foodItems.isEmpty()) {
                int itemsToChangeOnPromotion = 0;
                int numberOfFoodItems = 0;
                int numberOfExtraItems = 0;

                for (var ibasketItem : basket.getBasketItems()) {
                    if (!extrasItemsInBasket.stream().filter(item -> item.getProductCode().equals(ibasketItem.getProductCode())
                            && item.isExtraItem()).collect(Collectors.toList()).isEmpty()) {
                        itemsToChangeOnPromotion += ibasketItem.getUnits();
                    }
                    if (!foodItems.stream().filter(item -> item.getProductCode().equals(ibasketItem.getProductCode())
                            && item.isFoodItem()).collect(Collectors.toList()).isEmpty()) {
                        numberOfFoodItems += ibasketItem.getUnits();
                    }

                    if (!extraItems.stream().filter(item -> item.getProductCode().equals(ibasketItem.getProductCode())
                            && item.isExtraItem()).collect(Collectors.toList()).isEmpty()) {
                        numberOfExtraItems += ibasketItem.getUnits();
                    }

                }

                if (numberOfFoodItems < numberOfExtraItems) {
                    itemsToChangeOnPromotion = numberOfExtraItems - numberOfFoodItems;
                } else if (numberOfFoodItems == numberOfExtraItems) {
                    itemsToChangeOnPromotion = 0;
                }

                for (var basketItem : basket.getBasketItems()) {
                    if (basket.getNumberOfFreeBeverages() == 0 &&
                            extrasItemsInBasket.stream().filter(item -> item.getProductCode().equals(basketItem.getProductCode())).findAny().isPresent()) {

                        basketItem.setPromotionsUnits(Optional.of(itemsToChangeOnPromotion));
                        basketItem.setIncludeInPromotion(true);
                    }
                }

            }
        }

    }

    public double calculateBasketTotal(List<Product> shoppingBasketMatchedProducts, Basket basket) {

        if (basket.getNumberOfFreeBeverages() > 0) {
            var cheapestProductList = shoppingBasketMatchedProducts.stream()
                    .filter(p -> p.isRewardItem()).collect(Collectors.toList());
            Collections.sort(cheapestProductList, new Comparator<Product>() {
                public int compare(Product o1, Product o2) {
                    return Double.compare(o1.getPrice(), o2.getPrice());
                }
            });

            var cheapestProduct = cheapestProductList.get(0);

            for (var item : basket.getBasketItems()) {
                if (item.getProductCode().equals(cheapestProduct.getProductCode())) {
                    item.setPromotionsUnits(Optional.of(item.getUnits() - basket.getNumberOfFreeBeverages()));
                    break;
                }
            }

        }
        var basketTotal = basket.getBasketItems().stream()
                .map(shoppingBasketItem -> {
                    var product = Product.findProduct(shoppingBasketItem.getProductCode(), shoppingBasketMatchedProducts, false);

                    if (product.isEmpty()) {
                        return 0.0;
                    } else if (product.get().isExtraItem() &&
                            shoppingBasketItem.isIncludeInPromotion() && basket.getNumberOfFreeBeverages() == 0) {
                        return abs(shoppingBasketItem.getPromotionsUnits().get().intValue() * product.get().getPrice());
                    } else if (!product.get().isExtraItem() && !shoppingBasketItem.isIncludeInPromotion() && shoppingBasketItem.getPromotionsUnits().isPresent()) {
                        return abs(shoppingBasketItem.getPromotionsUnits().get().intValue()) * product.get().getPrice();
                    } else {
                        return shoppingBasketItem.getUnits() * product.get().getPrice();
                    }
                }).reduce(0.0, (accumulator, element) -> accumulator + element);

        return basketTotal;
    }

    public PromotionResult stampsAwarded(List<Product> shoppingBasketMatchedProducts, Basket basket) {

        var eligibleItems = shoppingBasketMatchedProducts.stream()
                .map(product -> {
                    var eligibleRewardItem = 0;
                    if (product.isRewardItem()) {
                        eligibleRewardItem = basket.getBasketItems().stream()
                                .filter(x -> x.getProductCode().equals(product.getProductCode()))
                                .map(b -> b.getUnits()).collect(Collectors.toList())
                                .stream().reduce(0, (accumulator, element) -> accumulator + element);
                    }
                    return eligibleRewardItem;
                }).reduce(0, (aggregator, item) -> aggregator + item);

        return new PromotionResult(eligibleItems);
    }
}
