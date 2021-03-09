package com.example.bitcointrader.Request;

import android.content.Context;

import com.example.bitcointrader.Entities.Coin;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestRetriever {

    public void getCoin(String url, Context context, final RequestCallBack callBack) {
        RequestSingleton.getInstance(context).addToRequestQueue(url, new RequestListener<String>() {
            @Override
            public void getResult(String object) {
                if (!object.isEmpty()) {
                    Gson gson = new Gson();
                    Coin coin = gson.fromJson(object, Coin.class);
                    callBack.onSuccess(coin);
                }
            }
        });
    }

    public void getCoinList(String url, Context context, final RequestCallBack callBack) {
        RequestSingleton.getInstance(context).addListToRequestQueue(url, new RequestListener<String>() {
            @Override
            public void getResult(String object) {
                if (!object.isEmpty()) {
                    Gson gson = new Gson();
                    Coin[] lastMonth = gson.fromJson(object, Coin[].class);
                    List<Coin> coins = new ArrayList<>();
                    coins.addAll(Arrays.asList(lastMonth));
                    callBack.onSuccess(coins);
                }
            }
        });
    }


}
