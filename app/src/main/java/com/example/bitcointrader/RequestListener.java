package com.example.bitcointrader;

public interface RequestListener<T> {
    public void getResult(T object);
}
