package com.example.bitcointrader;



public class Bitcoin {
    private String date;
    private double price;

    public Bitcoin(String date, double price) {
        this.date = date;
        this.price = price;
    }

    public Bitcoin() {
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
        return "" +price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Bitcoin{" +
                "date=" + date +
                ", price=" + price +
                '}';
    }
}
