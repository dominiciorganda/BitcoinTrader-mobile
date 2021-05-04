package com.example.bitcointrader.Entities;

public class User {
    private String authenticationToken;
    private String username;
    private String money;

    public User(String authenticationToken, String username, String money) {
        this.authenticationToken = authenticationToken;
        this.username = username;
        this.money = money;
    }

    public User(String token, String username) {
        this.authenticationToken = token;
        this.username = username;
    }

    public User() {
    }

    public String getToken() {
        return authenticationToken;
    }

    public void setToken(String token) {
        this.authenticationToken = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
