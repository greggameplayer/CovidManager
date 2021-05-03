package com.epsi.covidmanager.model.beans;

public class Token {
    static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Token.token = token;
    }
}
