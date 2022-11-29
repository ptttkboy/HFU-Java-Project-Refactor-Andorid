package com.example.project_demo1.network;

/**
 * 所有此專案連線的API位址
 */
public enum ApiConfig {

    LOGIN("login"),
    REGISTRATION("v1/members"),
    GET_RESTAURANTS("v1/restaurants"),
    GET_LATEST("v1/restaurants/latest");

    private static final String BASE_URL_EMULATOR = "http://10.0.2.2:8080/food-map/api/";
    private final String url;

    ApiConfig(String endpoint) {
        url = BASE_URL_EMULATOR + endpoint;
    }

    public String getUrl() {
        return url;
    }
}
