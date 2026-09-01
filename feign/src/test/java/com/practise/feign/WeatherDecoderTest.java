package com.practise.feign;

import com.practise.feign.decoder.WeatherErrorDecoder;
import com.practise.feign.exceptions.WeatherException;
import feign.Request;
import feign.Response;
import feign.RetryableException;
import net.bytebuddy.agent.VirtualMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.ResponseEntity;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class WeatherDecoderTest {
        ObjectMapper objectMapper;
        WeatherErrorDecoder weatherErrorDecoder;
    static Stream<Arguments> notFoundData() {
        return Stream.of(
                Arguments.of(404, "City not found"),
                Arguments.of(404, "Location not found"),
                Arguments.of(404, "Weather data not found")
        );
    }

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
        weatherErrorDecoder = new WeatherErrorDecoder(objectMapper);
    }

    @ParameterizedTest
    @MethodSource("notFoundData")
    void shouldReturnRetryableException(int status,
                                        String message){


        Request request = Request.create(
                Request.HttpMethod.GET,
                "http://localhost",
                Map.of(),
                null,
                null,
                null
        );

        Response response = Response.builder()
                .status(status)
                .reason("Not found")
                .request(request)
                .body(message,
                        StandardCharsets.UTF_8)
                .build();

        Exception result = weatherErrorDecoder.decode( "WeatherClient#getWeather",response);
        assertInstanceOf(RetryableException.class,result);

        RetryableException retryableException = (RetryableException) result;

        assertEquals(404,retryableException.status());




    }

    static Stream<Arguments> errorResponseData() {
        return Stream.of(
                Arguments.of(
                        400,
                        "{\"code\":\"400\",\"message\":\"Invalid City\"}",
                        "Invalid City"
                ),
                Arguments.of(
                        400,
                        "{\"code\":\"400\",\"message\":\"Incorrect web address\"}",
                        "Incorrect web address"
                ),
                Arguments.of(
                        400,
                        "{\"code\":\"400\",\"message\":\"Broken url\"}",
                        "Broken url"
                )
        );
    }
    @ParameterizedTest
    @MethodSource("errorResponseData")

    void shouldReturnWeatherException(int status, String errorJson,String message){
        Request request = Request.create(
                Request.HttpMethod.GET,
                "http://localhost",
                Map.of(),
                null,
                null,
                null
        );



        Response response = Response.builder()
                .status(status)
                .reason("Bad request")
                .request(request)
                .body(errorJson,StandardCharsets.UTF_8)
                .build();

        Exception result = weatherErrorDecoder.decode(
                "WeatherClient#getWeather",
                response);

        assertInstanceOf(WeatherException.class,result);

        WeatherException exception = (WeatherException) result;
        assertEquals(status,exception.getStatus());
        assertEquals(message,exception.getMessage());
    }

    // does it reach the exception when invalid json was sent as a parameter;

    static Stream<Arguments> invalidJsonData() {
        return Stream.of(
                Arguments.of(400, "Invalid JSON", "Failed to read the api response"),
                Arguments.of(500, "This is not JSON", "Failed to read the api response"),
                Arguments.of(502, "{broken-json", "Failed to read the api response")
        );
    }
    @ParameterizedTest
    @MethodSource("invalidJsonData")
    void shouldReturnWeatherExceptionForInvalidJson(
            int status,
            String invalidJson,
            String expectedMessage
    ) {

        Request request = Request.create(
                Request.HttpMethod.GET,
                "http://localhost",
                Map.of(),
                null,
                null,
                null
        );

        Response response = Response.builder()
                .status(status)
                .reason("Error")
                .request(request)
                .body(invalidJson, StandardCharsets.UTF_8)
                .build();

        Exception result = weatherErrorDecoder.decode(
                "WeatherClient#getWeather",
                response
        );

        assertInstanceOf(WeatherException.class, result);

        WeatherException exception =
                (WeatherException) result;

        assertEquals(status, exception.getStatus());
        assertEquals(expectedMessage, exception.getMessage());
    }

    //when the body was not sent
    static Stream<Arguments> nullBodyData() {
        return Stream.of(
                Arguments.of(400, "Weather api request failed"),
                Arguments.of(500, "Weather api request failed"),
                Arguments.of(503, "Weather api request failed")
        );
    }
    @ParameterizedTest
    @MethodSource("nullBodyData")
    void shouldReturnWeatherExceptionWhenBodyIsNull(
            int status,
            String expectedMessage
    ) {

        Request request = Request.create(
                Request.HttpMethod.GET,
                "http://localhost",
                Map.of(),
                null,
                null,
                null
        );

        Response response = Response.builder()
                .status(status)
                .reason("Error")
                .request(request)
                .build();

        Exception result = weatherErrorDecoder.decode(
                "WeatherClient#getWeather",
                response
        );

        assertInstanceOf(WeatherException.class, result);

        WeatherException exception =
                (WeatherException) result;

        assertEquals(status, exception.getStatus());
        assertEquals(expectedMessage, exception.getMessage());
    }

}
