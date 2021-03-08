package com.example.bitcointrader;

public interface RequestCallBack<T> {
    public void onSuccess(T coin);
}
