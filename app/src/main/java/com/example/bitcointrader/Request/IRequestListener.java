package com.example.bitcointrader.Request;

public interface IRequestListener<T> {
    public void getResult(T object);
}
