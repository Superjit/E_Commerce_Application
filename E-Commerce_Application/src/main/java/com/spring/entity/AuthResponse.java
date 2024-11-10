package com.spring.entity;



public class AuthResponse {
    private String token;

    // Constructor
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    // Setter (if needed)
    public void setToken(String token) {
        this.token = token;
    }
}

