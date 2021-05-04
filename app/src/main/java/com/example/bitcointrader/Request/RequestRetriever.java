package com.example.bitcointrader.Request;

import android.content.Context;

import com.example.bitcointrader.Entities.Coin;
import com.example.bitcointrader.Entities.CommonUtils;
import com.example.bitcointrader.Entities.Transaction;
import com.example.bitcointrader.Entities.User;
import com.example.bitcointrader.Entities.WalletCoin;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestRetriever {

    public void getCoin(String url, Context context, final IRequestCallBack callBack) {
        RequestSingleton.getInstance(context).addToRequestQueue(url, new IRequestListener<String>() {
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

    public void getCoinList(String url, Context context, final IRequestCallBack callBack) {
        RequestSingleton.getInstance(context).addListToRequestQueue(url, new IRequestListener<String>() {
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

    public void login(String url, Context context, final IRequestCallBack callBack, JSONObject body) {
        RequestSingleton.getInstance(context).addLoginToRequestQueue(url, new IRequestListener<String>() {
            @Override
            public void getResult(String object) {
                if (!object.isEmpty()) {
                    if (object.equals("error"))
                        callBack.onSuccess(object);
                    else {
                        Gson gson = new Gson();
                        User user = gson.fromJson(object, User.class);
                        CommonUtils.putPrefString(context, "token", user.getToken());
                        CommonUtils.putPrefString(context, "username", user.getUsername());
                        CommonUtils.putPrefString(context, "money", user.getMoney());
                        callBack.onSuccess("succes");
                    }
                }
            }
        }, body);
    }

    public void register(String url, Context context, final IRequestCallBack callBack, JSONObject body) {
        RequestSingleton.getInstance(context).addRegisterToRequestQueue(url, new IRequestListener<String>() {
            @Override
            public void getResult(String object) {
                System.out.println(object);
                if (!object.isEmpty()) {
                    callBack.onSuccess(object);
                }
            }
        }, body);
    }

    public void transaction(String url, Context context, final IRequestCallBack callBack, JSONObject body) {
        RequestSingleton.getInstance(context).addStringRequestToRequestQueue(url, new IRequestListener<String>() {
            @Override
            public void getResult(String object) {
                System.out.println(object);
                if (!object.isEmpty()) {
                    callBack.onSuccess(object);
                }
            }
        }, body);
    }

    public void getMoney(String url, Context context, final IRequestCallBack callBack) {
        RequestSingleton.getInstance(context).addGetStringRequestToRequestQueue(url, new IRequestListener<String>() {
            @Override
            public void getResult(String object) {
                if (!object.isEmpty()) {
                    CommonUtils.putPrefString(context, "money", object);
                    callBack.onSuccess("succes");
                } else callBack.onSuccess("error");
            }
        });
    }

    public void getWallet(String url, Context context, final IRequestCallBack callBack) {
        RequestSingleton.getInstance(context).addListToRequestQueue(url, new IRequestListener<String>() {
            @Override
            public void getResult(String object) {
                if (!object.isEmpty()) {
                    Gson gson = new Gson();
                    WalletCoin[] walletCoins = gson.fromJson(object, WalletCoin[].class);
                    List<WalletCoin> coins = new ArrayList<>();
                    coins.addAll(Arrays.asList(walletCoins));
                    callBack.onSuccess(coins);
                }
            }
        });
    }

    public void getTransactions(String url, Context context, final IRequestCallBack callBack) {
        RequestSingleton.getInstance(context).addListToRequestQueue(url, new IRequestListener<String>() {
            @Override
            public void getResult(String object) {
                if (!object.isEmpty()) {
                    Gson gson = new Gson();
                    Transaction[] transactions = gson.fromJson(object, Transaction[].class);
                    List<Transaction> transactionList = new ArrayList<>();
                    transactionList.addAll(Arrays.asList(transactions));
                    callBack.onSuccess(transactionList);
                }
            }
        });
    }


}
