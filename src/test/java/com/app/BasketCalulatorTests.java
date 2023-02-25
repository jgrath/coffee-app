package test.java.com.app;

import com.app.BasketCalculator;

import com.app.CoffeeCornerApplication;
import com.app.domain.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BasketCalulatorTests {
    List listOfAllAvailableProducts = List.of(
            new RewardItem("coffee-small", 1, 2.50),
            new RewardItem("coffee-medium", 2, 3.00),
            new RewardItem("coffee-large", 3, 3.50),
            new RewardItem("orange-juice-0.25", 4, 3.95),
            new FoodItem("bacon-roll", 5, 4.50),
            new AddOnItem("extra-milk", 6, 0.30),
            new AddOnItem("foamed-milk", 7, 0.50),
            new AddOnItem("special-roast-coffee", 8, 0.90)
    );

    @Test
    public void testEmptyBasket() {
        List basketItems = new ArrayList<>();
        BasketCalculator basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(new PromotionResult(0), basketCalculator.stampsAwarded(listOfAllAvailableProducts, new Basket(basketItems, 0)));
    }

    @Test
    public void testRewardsInBasket() {

        var basketItems = List.of(new BasketItem("coffee-large", 1),
                new BasketItem("coffee-medium", 3),
                new BasketItem("bacon-roll", 30),
                new BasketItem("extra-milk", 100),
                new BasketItem("unkowncoffee-large", 1));
        BasketCalculator basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(new PromotionResult(4), basketCalculator.stampsAwarded(listOfAllAvailableProducts, new Basket(basketItems, 0)));

    }

    @Test
    public void testOneBeverageWithSnackInBasket() {

        var basketItems = List.of(
                new BasketItem("coffee-medium", 1),
                new BasketItem("extra-milk", 1),
                new BasketItem("bacon-roll", 1));
        var basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(7.5, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-medium", 1),
                new BasketItem("extra-milk", 3),
                new BasketItem("bacon-roll", 1));
        basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(8.1, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-medium", 2),
                new BasketItem("extra-milk", 3),
                new BasketItem("bacon-roll", 1));
        basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(11.1, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

    }

    @Test
    public void testEveryFifthBeverageFree() {

        var basketItems = List.of(
                new BasketItem("coffee-medium", 4));
        var basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(12, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-medium", 5));
        basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(12, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-medium", 4),
                new BasketItem("coffee-large", 1));
        basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(12.50, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-medium", 5),
                new BasketItem("coffee-large", 1));
        basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(15.50, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-medium", 1),
                new BasketItem("coffee-large", 4));
        basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(14.00, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-small", 1),
                new BasketItem("coffee-medium", 1),
                new BasketItem("coffee-large", 4));
        basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(17.00, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-small", 2),
                new BasketItem("coffee-medium", 2),
                new BasketItem("coffee-large", 1),
                new BasketItem("extra-milk", 1));
        basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(12.30, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

    }

    @Test
    public void testCombinationOffersNotAllowed() {
        var basketItems = List.of(
                new BasketItem("coffee-small", 2),
                new BasketItem("coffee-medium", 2),
                new BasketItem("coffee-large", 1),
                new BasketItem("bacon-roll", 1),
                new BasketItem("invalid-product", 11111),
                new BasketItem("extra-milk", 1));
        var basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(16.80, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-small", 2),
                new BasketItem("coffee-medium", 2),
                new BasketItem("coffee-large", 1),
                new BasketItem("bacon-roll", 1),
                new BasketItem("extra-milk", 2));
        assertEquals(17.10, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);
    }

    @Test
    public void testExtraProductOffer() {
        var basketItems = List.of(
                new BasketItem("coffee-small", 1),
                new BasketItem("bacon-roll", 1),
                new BasketItem("extra-milk", 1));
        var basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(7, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-small", 2),
                new BasketItem("bacon-roll", 2),
                new BasketItem("extra-milk", 2));
        assertEquals(14.00, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

        basketItems = List.of(
                new BasketItem("coffee-medium", 1),
                new BasketItem("extra-milk", 2),
                new BasketItem("bacon-roll", 1));
        basketCalculator = new BasketCalculator(listOfAllAvailableProducts);
        assertEquals(7.8, basketCalculator.calculateBasket(new Basket(basketItems, 0)), 0.0);

    }

    @Test
    public void testBuildShoppingBasketUsingApplicationExecutor() {

        var basketItems = List.of(new BasketItem("coffee-large", 1),
                new BasketItem("extra-milk", 1),
                new BasketItem("bacon-roll", 1));

        CoffeeCornerApplication application = new CoffeeCornerApplication(listOfAllAvailableProducts);

        var total = application.execute(basketItems);

        assertEquals(8.0, total, 0.0);
    }
}