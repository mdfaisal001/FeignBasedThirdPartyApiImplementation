package com.practise.feign.exceptions;

public class WeatherException extends RuntimeException{

    private final int status;

    public WeatherException(int status, String message){
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
