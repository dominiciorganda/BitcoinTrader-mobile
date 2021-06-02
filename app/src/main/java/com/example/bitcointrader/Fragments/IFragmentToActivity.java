package com.example.bitcointrader.Fragments;

import com.example.bitcointrader.Entities.ChartType;

public interface IFragmentToActivity {
    void communicate(String data);

    void communicate(String data, ChartType chartType);
}
