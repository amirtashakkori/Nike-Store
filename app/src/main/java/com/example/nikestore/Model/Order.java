package com.example.nikestore.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.nikestore.Model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    long id;
    String first_name;
    String last_name;
    String postal_code;
    String phone;
    String address;
    String date;
    int payable;
    int total;
    int user_id;
    List<OrderItem> order_items;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPayable() {
        return payable;
    }

    public void setPayable(int payable) {
        this.payable = payable;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<OrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<OrderItem> order_items) {
        this.order_items = order_items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.postal_code);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.date);
        dest.writeInt(this.payable);
        dest.writeInt(this.total);
        dest.writeInt(this.user_id);
        dest.writeList(this.order_items);
    }

    protected Order(Parcel in) {
        this.id = in.readLong();
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.postal_code = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.date = in.readString();
        this.payable = in.readInt();
        this.total = in.readInt();
        this.user_id = in.readInt();
        this.order_items = new ArrayList<>();
        in.readList(this.order_items, OrderItem.class.getClassLoader());
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
