package com.bridgelabz.fundoonotes.model;

import org.springframework.http.HttpStatus;

public class Response {

    //Variable
    private int statusCode;
    private String statusMessage;

    //Parameterised Constructor.
    public Response(int statusCode, String message) {
        super();
        this.statusCode=statusCode;
        this.statusMessage=message;
    }

    //Empty Constructor with super.
    public Response() {
        super();
    }

    //Getters and Setters
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

}
