package com.bridgelabz.fundoonotes.model;

public class ResponseToken {

    //Variables
    private int statusCode;
    private String statusMessage;
    private String token;

    //Constructor
    public ResponseToken() {
    }

    //Getters and setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
