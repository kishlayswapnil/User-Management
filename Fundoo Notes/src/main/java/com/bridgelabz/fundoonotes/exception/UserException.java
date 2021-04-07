package com.bridgelabz.fundoonotes.exception;

public class UserException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    int errorCode;
    public UserException(int errorCode, String textMessage) {
        super(textMessage);
        this.errorCode=errorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }
}
