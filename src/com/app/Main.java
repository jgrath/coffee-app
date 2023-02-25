package com.app;

import com.app.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

	List<Product> listOfAllAvailableProducts = List.of(
		new RewardItem("coffee-small", 1, 2.50),
		new RewardItem("coffee-medium", 2, 3.00),
		new RewardItem("coffee-large", 3, 3.50),
		new RewardItem("orange-juice-0.25", 4, 3.95),
		new FoodItem("bacon-roll", 5, 4.50),
		new AddOnItem("extra-milk", 6, 0.30),
		new AddOnItem("foamed-milk", 7, 0.50),
		new AddOnItem("special-roast-coffee", 8, 0.90)
	);

	/**
	 * Application to compute shopping baskets.
	 * Sample Input: "coffee-large:1,extra-milk:2,bacon-roll:1"
	 */
	public static void main(String args[]) {
		new Main().mainAppImpl(args);
	}

	private void mainAppImpl(String[] args) {

		List<String> productCodeStrings = listOfAllAvailableProducts.stream()
			.map(x -> x.getProductCode())
			.collect(Collectors.toList());


		//Console c = System.console();
		System.out.println("Enter you product choices from the following options: ");
		System.out.println("In the format coffee-large,extra-milk,bacon-roll followed by [Enter]");

		List<String> inputStringList = java.util.Arrays.asList(args[0].split(","));
		List<BasketItem> basketItems = new ArrayList<>();

		for (var productString : inputStringList) {

			String productCode = productString;
			int quantity = 1;

			if(productString.contains(":")) {
				String[] splitItem = productString.split(":");
				productCode = splitItem[0];
				quantity = Integer.valueOf(splitItem[1]);
			}
			if (!Product.findProduct(productCode, listOfAllAvailableProducts, false).isPresent()) {
				System.out.printf("\ncom.app.domain.Product [%s] is invalid and will be ignored\n\n", productString);
			} else {
				basketItems.add(new BasketItem(productCode, quantity));
			}
		}

		CoffeeCornerApplication application = new CoffeeCornerApplication(listOfAllAvailableProducts);

		application.execute(basketItems);
	}
}
