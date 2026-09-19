package com.example.loanbackend.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String phone;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(String token, Long id, String phone, String message) {
        this.token = token;
        this.type = "Bearer";
        this.id = id;
        this.phone = phone;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
