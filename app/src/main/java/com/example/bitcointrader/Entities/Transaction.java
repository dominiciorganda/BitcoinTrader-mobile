package com.example.bitcointrader.Entities;

public class Transaction {
    private String transactionDate;
    private CoinTypes coin;
    private double amount;
    private double actualPrice;
    private double paidPrice;
    private TransactionType type;


    public Transaction(CoinTypes coin, double amount, double actualPrice, double paidPrice, TransactionType type, String transactionDate) {
        this.coin = coin;
        this.amount = amount;
        this.actualPrice = actualPrice;
        this.paidPrice = paidPrice;
        this.type = type;
        this.transactionDate = transactionDate;
    }

    public Transaction() {
    }

    public CoinTypes getCoin() {
        return coin;
    }

    public void setCoin(CoinTypes coin) {
        this.coin = coin;
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

    public double getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(double paidPrice) {
        this.paidPrice = paidPrice;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "coin=" + coin +
                ", amount=" + amount +
                ", actualPrice=" + actualPrice +
                ", paidPrice=" + paidPrice +
                ", type=" + type +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
