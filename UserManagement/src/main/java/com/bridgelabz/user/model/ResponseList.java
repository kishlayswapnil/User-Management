package com.bridgelabz.user.model;

import lombok.Data;

@Data
public class ResponseList {
        //Variables
        private int statusCode;
        private String statusMessage;
        private Object data;

    public ResponseList(int statusCode, String statusMessage, Object data) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data = data;
    }
}
