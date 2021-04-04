package com.example.bitcointrader.util;

public enum CoinUrls {

    BITCOIN(Urls.BITCOIN), ETHEREUM(Urls.ETHEREUM), ELROND(Urls.ELROND),
    LITECOIN(Urls.LITECOIN), DASH(Urls.DASH), DOGECOIN(Urls.DOGECOIN),
    BINANCECOIN(Urls.BINANCECOIN), BITCOINCASH(Urls.BITCOINCASH), FILECOIN(Urls.FILECOIN);

    private final String url;

    CoinUrls(String url) {
        this.url = url;
    }

    public static CoinUrls find(String string) {
        if (string.equals(Urls.BITCOIN))
            return BITCOIN;
        if (string.equals(Urls.ETHEREUM))
            return ETHEREUM;
        if (string.equals(Urls.ELROND))
            return ELROND;
        if (string.equals(Urls.LITECOIN))
            return LITECOIN;
        if (string.equals(Urls.DASH))
            return DASH;
        if (string.equals(Urls.DOGECOIN))
            return DOGECOIN;
        if (string.equals(Urls.BINANCECOIN))
            return BINANCECOIN;
        if (string.equals(Urls.BITCOINCASH))
            return BITCOINCASH;
        if (string.equals(Urls.FILECOIN))
            return FILECOIN;
        return null;
    }
}
