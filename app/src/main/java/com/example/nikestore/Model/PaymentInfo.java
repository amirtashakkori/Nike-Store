package com.example.nikestore.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentInfo implements Parcelable {
    public String totalPrice;
    public String shipingCost;
    public String payablePrice;

    public PaymentInfo(String totalPrice, String shipingCost, String payablePrice) {
        this.totalPrice = totalPrice;
        this.shipingCost = shipingCost;
        this.payablePrice = payablePrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShipingCost() {
        return shipingCost;
    }

    public void setShipingCost(String shipingCost) {
        this.shipingCost = shipingCost;
    }

    public String getPayablePrice() {
        return payablePrice;
    }

    public void setPayablePrice(String payablePrice) {
        this.payablePrice = payablePrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.totalPrice);
        dest.writeString(this.shipingCost);
        dest.writeString(this.payablePrice);
    }

    public void readFromParcel(Parcel source) {
        this.totalPrice = source.readString();
        this.shipingCost = source.readString();
        this.payablePrice = source.readString();
    }

    public PaymentInfo() {
    }

    protected PaymentInfo(Parcel in) {
        this.totalPrice = in.readString();
        this.shipingCost = in.readString();
        this.payablePrice = in.readString();
    }

    public static final Parcelable.Creator<PaymentInfo> CREATOR = new Parcelable.Creator<PaymentInfo>() {
        @Override
        public PaymentInfo createFromParcel(Parcel source) {
            return new PaymentInfo(source);
        }

        @Override
        public PaymentInfo[] newArray(int size) {
            return new PaymentInfo[size];
        }
    };
}
