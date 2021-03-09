package com.example.bitcointrader.Request;

public interface RequestListener<T> {
    public void getResult(T object);
}
