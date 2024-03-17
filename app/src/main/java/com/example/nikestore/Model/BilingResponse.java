package com.example.nikestore.Model;

public class BilingResponse {
    public int order_id;
    public String bank_gateway_url;

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getBank_gateway_url() {
        return bank_gateway_url;
    }
}
