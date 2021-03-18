package com.mediscreen.risk.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RiskNotDefinedException extends RuntimeException{
    public RiskNotDefinedException(String message)
    {
        super(message);
    }
}


