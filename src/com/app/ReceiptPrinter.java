package com.app;

import java.util.List;

public class ReceiptPrinter {

    void print(List<String> lineItems, double total) {
        lineItems.stream().forEach(lineItem -> System.out.println(lineItem));
        System.out.printf("Total is: %10.2f", total);
    }

}