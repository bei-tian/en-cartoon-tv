package com.encartoon.encartoon;

import android.app.Application;

public class Base extends Application {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void onCreate() {
        baseUrl = "http://api.en-cartoon.com/v2/";
        super.onCreate();
    }
}
