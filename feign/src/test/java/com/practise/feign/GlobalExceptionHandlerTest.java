package com.practise.feign;

import com.practise.feign.dto.ClientErrorResponse;
import com.practise.feign.exceptions.GlobalExceptionHandler;
import com.practise.feign.exceptions.WeatherException;
import feign.Request;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {
    GlobalExceptionHandler handler;
    @BeforeEach
    void setHandler(){
        handler = new GlobalExceptionHandler();
    }
    @Test
    void shouldHandleWeatherException(){


        WeatherException exception = new WeatherException(404,"City not found");
        ResponseEntity<ClientErrorResponse> response = handler.handleWeatherApiException(exception);

        assertEquals(404,response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(404,response.getBody().status());
        assertEquals("City not found",response.getBody().message());
    }

    @Test

    void shouldHandleRetryableException(){
        RetryableException exception =
                new RetryableException(
                        503,
                        "Weather Service not found",
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
        assertEquals("Weather Service not found",exception.getMessage());
        assertNotNull(response.getBody());
        assertEquals(503,response.getBody().status());
        assertEquals("Weather service is temporary unavailabe",response.getBody().message());
    }

}
