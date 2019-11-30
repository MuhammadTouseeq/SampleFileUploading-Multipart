package com.pakdev.fileuploading.api;


import android.support.annotation.Nullable;

public class WebResponse<T> {
    @Nullable
    private String Message;
    @Nullable
    private T Result;
    @Nullable
    private String Response;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getResult() {
        return Result;
    }

    public void setResult(T result) {
        Result = result;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public boolean isSuccess() {
        if (Response.equalsIgnoreCase("2000")) {
            return true;
        } else {
            return false;
        }
    }
}
