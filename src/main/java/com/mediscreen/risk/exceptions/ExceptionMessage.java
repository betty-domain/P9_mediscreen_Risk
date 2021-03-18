package com.mediscreen.risk.exceptions;

public class ExceptionMessage {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public String getMessage()
    {
        return this.message;
    }
}
