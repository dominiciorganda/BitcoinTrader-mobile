package com.example.bitcointrader.Entities;

public class WalletCoin {
    private CoinTypes coinName;
    private double amount;
    private double actualPrice;
    private double value;
    private double paid;

    public WalletCoin(CoinTypes coinName, double amount, double actualPrice, double value, double paid) {
        this.coinName = coinName;
        this.amount = amount;
        this.actualPrice = actualPrice;
        this.value = value;
        this.paid = paid;
    }

    public WalletCoin() {
    }

    public CoinTypes getCoinName() {
        return coinName;
    }

    public void setCoinName(CoinTypes coinName) {
        this.coinName = coinName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "WalletCoin{" +
                "coinName=" + coinName +
                ", amount=" + amount +
                ", actualPrice=" + actualPrice +
                ", value=" + value +
                ", paid=" + paid +
                '}';
    }
}
