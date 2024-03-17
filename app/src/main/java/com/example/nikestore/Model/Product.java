package com.example.nikestore.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String image;
    public String title;
    public int previous_price;
    public int price;
    public int status;
    public int discount;
    public boolean favorite = false;

    public static final String SORT_LATEST = "0";
    public static final String SORT_POPULAR = "1";
    public static final String SORT_PRICE_DESC = "2";
    public static final String SORT_PRICE_ASC = "3";

    public Product(int id, String image, String title, int previous_price, int price) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.previous_price = previous_price;
        this.price = price;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public int getPrevious_price() {
        return previous_price;
    }

    public int getPrice() {
        return price;
    }

    public int getStatus() {
        return status;
    }

    public int getDiscount() {
        return discount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.image);
        dest.writeString(this.title);
        dest.writeInt(this.previous_price);
        dest.writeInt(this.price);
        dest.writeInt(this.status);
        dest.writeInt(this.discount);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.image = source.readString();
        this.title = source.readString();
        this.previous_price = source.readInt();
        this.price = source.readInt();
        this.status = source.readInt();
        this.discount = source.readInt();
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.id = in.readInt();
        this.image = in.readString();
        this.title = in.readString();
        this.previous_price = in.readInt();
        this.price = in.readInt();
        this.status = in.readInt();
        this.discount = in.readInt();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
