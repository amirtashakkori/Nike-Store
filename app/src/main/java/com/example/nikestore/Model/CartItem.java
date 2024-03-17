package com.example.nikestore.Model;

public class CartItem {
    public int cart_item_id;
    public int count;
    public Product product;

    public int getCart_item_id() {
        return cart_item_id;
    }

    public int getCount() {
        return count;
    }

    public Product getProduct() {
        return product;
    }
}
