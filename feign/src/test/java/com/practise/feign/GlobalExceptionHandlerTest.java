package com.practise.feign;

import com.practise.feign.dto.ClientErrorResponse;
import com.practise.feign.exceptions.GlobalExceptionHandler;
import com.practise.feign.exceptions.WeatherException;
import feign.Request;
import feign.RetryableException;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.ResponseEntity;


import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {
    GlobalExceptionHandler handler;
    @BeforeEach
    void setHandler(){
        handler = new GlobalExceptionHandler();
    }


    static Stream<Arguments> weatherExceptionData(){
        return Stream.of(
                Arguments.of(404,"City not found"),
                Arguments.of(404,"Invalid City"),
                Arguments.of(500,"Weather Service failed")
        );
    }
    @ParameterizedTest
    @MethodSource("weatherExceptionData")
    void shouldHandleWeatherException(int status,String message){


        WeatherException exception = new WeatherException(status,message);
        ResponseEntity<ClientErrorResponse> response = handler.handleWeatherApiException(exception);

        assertEquals(status,response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(status,response.getBody().status());
        assertEquals(message,response.getBody().message());
    }

    static Stream<Arguments> retryableData(){
        return Stream.of(
                Arguments.of(503, "Weather Service not found"),
                Arguments.of(500, "Internal server error"),
                Arguments.of(429, "Too many requests")
        );
    }
    @ParameterizedTest
    @MethodSource("retryableData")
    void shouldHandleRetryableException(int status,String message){
        RetryableException exception =
                new RetryableException(
                        status,
                        message,
                        Request.HttpMethod.GET,
                        (Long) null,
                        Request.create(
                                Request.HttpMethod.GET,
                                "http://localhost",
                                java.util.Map.of(),
                                null,
                                null,
                                null
                        )
                );
        ResponseEntity<ClientErrorResponse> response = handler.handleRetryerException(exception);

        assertEquals(503,response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(503,response.getBody().status());
        assertEquals("Weather service is temporary unavailabe",response.getBody().message());
    }

}
