package com.example.bitcointrader.Entities;

import com.example.bitcointrader.R;
import com.example.bitcointrader.util.Urls;

public enum CoinTypes {
    BITCOIN, BINANCECOIN, BITCOINCASH, DASH, DOGECOIN, ELROND, ETHEREUM, FILECOIN, LITECOIN;

    public static String getName(CoinTypes value) {
        switch (value) {
            case BITCOIN:
                return "Bitcoin";
            case DASH:
                return "Dash";
            case ELROND:
                return "Elrond";
            case DOGECOIN:
                return "Dogecoin";
            case ETHEREUM:
                return "Ethereum";
            case FILECOIN:
                return "Filecoin";
            case LITECOIN:
                return "Litecoin";
            case BINANCECOIN:
                return "Binance Coin";
            case BITCOINCASH:
                return "Bitcoin Cash";
            default:
                return "None";
        }
    }

    public static int getDrawable(CoinTypes value) {
        switch (value) {
            case BITCOIN:
                return R.drawable.btc;
            case DASH:
                return R.drawable.dash;
            case ELROND:
                return R.drawable.erd;
            case DOGECOIN:
                return R.drawable.doge;
            case ETHEREUM:
                return R.drawable.eth;
            case FILECOIN:
                return R.drawable.fil;
            case LITECOIN:
                return R.drawable.ltc;
            case BINANCECOIN:
                return R.drawable.bnb;
            case BITCOINCASH:
                return R.drawable.bch;
            default:
                return 0;
        }
    }

    public static String getShortcut(CoinTypes value) {
        switch (value) {
            case BITCOIN:
                return "BTC";
            case DASH:
                return "DASH";
            case ELROND:
                return "ERD";
            case DOGECOIN:
                return "DOGE";
            case ETHEREUM:
                return "ETH";
            case FILECOIN:
                return "FIL";
            case LITECOIN:
                return "LTC";
            case BINANCECOIN:
                return "BNB";
            case BITCOINCASH:
                return "BCH";
            default:
                return "NONE";
        }
    }

    public static String getCoinUrl(CoinTypes value) {
        switch (value) {
            case BITCOIN:
                return Urls.BITCOIN;
            case DASH:
                return Urls.DASH;
            case ELROND:
                return Urls.ELROND;
            case DOGECOIN:
                return Urls.DOGECOIN;
            case ETHEREUM:
                return Urls.ETHEREUM;
            case FILECOIN:
                return Urls.FILECOIN;
            case LITECOIN:
                return Urls.LITECOIN;
            case BINANCECOIN:
                return Urls.BINANCECOIN;
            case BITCOINCASH:
                return Urls.BITCOINCASH;
            default:
                return "NONE";
        }
    }

}

