package com.practise.feign.exceptions;

import com.practise.feign.dto.ClientErrorResponse;
import com.practise.feign.dto.WeatherErrorResponse;
import feign.RetryableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.ref.Cleaner;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WeatherException.class)
    public ResponseEntity<ClientErrorResponse> handleWeatherApiException(WeatherException exception){
            ClientErrorResponse errorResponse = new ClientErrorResponse(exception.getStatus(),
                    exception.getMessage());

            return ResponseEntity.status(exception.getStatus())
                    .body(errorResponse);

    }
    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<ClientErrorResponse> handleRetryerException(RetryableException e){
        ClientErrorResponse response = new ClientErrorResponse(503,"Weather service is temporary unavailabe");

        return ResponseEntity.status(503)
                .body(response);
    }
}
