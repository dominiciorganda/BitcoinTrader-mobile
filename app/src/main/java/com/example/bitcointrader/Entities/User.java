package com.example.bitcointrader.Entities;

public class User {
    private String authenticationToken;
    private String username;

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
}
