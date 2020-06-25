package com.bridgelabz.user.exception;

public class TokenException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    int errorCode;

    public TokenException(String msg,int errorCode) {
        super(msg);
        this.errorCode=errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
