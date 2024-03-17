package com.example.nikestore.Model;

public class CheckoutResponse {
    boolean purchase_success;
    int payable_price;
    String payment_status;

    public boolean isPurchase_success() {
        return purchase_success;
    }

    public int getPayable_price() {
        return payable_price;
    }

    public String getPayment_status() {
        return payment_status;
    }
}
