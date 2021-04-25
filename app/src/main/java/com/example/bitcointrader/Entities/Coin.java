package com.example.bitcointrader.Entities;


import android.os.Parcel;
import android.os.Parcelable;

public class Coin implements Parcelable {
    private String date;
    private double price;

    public Coin(String date, double price) {
        this.date = date;
        this.price = price;
    }

    public Coin() {

    }

    public Coin(Parcel in) {
        this.setDate(in.readString());
        this.setPrice(in.readDouble());
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public String showPrice() {
        return "" + price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "date=" + date +
                ", price=" + price +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getDate());
        parcel.writeDouble(this.getPrice());
    }

    public static final Creator<Coin> CREATOR = new Creator<Coin>() {
        @Override
        public Coin createFromParcel(Parcel in) {
            return new Coin(in);
        }

        @Override
        public Coin[] newArray(int size) {
            return new Coin[size];
        }
    };
}
