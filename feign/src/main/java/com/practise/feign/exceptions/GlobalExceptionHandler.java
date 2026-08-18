package com.practise.feign.exceptions;

import com.practise.feign.dto.ClientErrorResponse;
import com.practise.feign.dto.WeatherErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WeatherException.class)
    public ResponseEntity<ClientErrorResponse> handleWeatherApiException(WeatherException exception){
            ClientErrorResponse errorResponse = new ClientErrorResponse(exception.getStatus(),
                    exception.getMessage());

            return ResponseEntity.status(exception.getStatus())
                    .body(errorResponse);

    }
}
