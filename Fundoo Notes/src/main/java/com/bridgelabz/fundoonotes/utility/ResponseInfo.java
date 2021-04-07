package com.bridgelabz.fundoonotes.utility;

import com.bridgelabz.fundoonotes.model.Response;
import com.bridgelabz.fundoonotes.model.ResponseToken;

public class ResponseInfo {
    public static Response getResponse(int statusCode, String statusMessage)
    {
        Response response = new Response();
        response.setStatusCode(statusCode);
        response.setStatusMessage(statusMessage);
        return response;
    }

    public static ResponseToken getResponseToken(int statusCode, String statusMessage, String token)
    {
        ResponseToken tokenResponse = new ResponseToken();
        tokenResponse.setStatusMessage(statusMessage);
        tokenResponse.setStatusCode(statusCode);
        tokenResponse.setToken(token);
        return tokenResponse;
    }
}
