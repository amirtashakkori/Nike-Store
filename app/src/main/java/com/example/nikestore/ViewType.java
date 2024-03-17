package com.example.nikestore;

public enum ViewType {
    ROW(0) , GRIDE(1) , LARGE(2);

    private int value;

    ViewType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
