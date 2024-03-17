package com.example.nikestore.Model;

import java.util.List;

public class CartResponse {
    public List<CartItem> cart_items;
    int payable_price;
    int shipping_cost;
    int total_price;

    public List<CartItem> getCart_items() {
        return cart_items;
    }

    public int getPayable_price() {
        return payable_price;
    }

    public int getShipping_cost() {
        return shipping_cost;
    }

    public int getTotal_price() {
        return total_price;
    }
}
