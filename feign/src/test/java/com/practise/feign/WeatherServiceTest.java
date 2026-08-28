package com.practise.feign;

import com.practise.feign.client.WeatherClient;
import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherQuery;
import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.exceptions.WeatherException;
import com.practise.feign.mapper.WeatherMapper;
import com.practise.feign.service.WeatherService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherClient weatherClient;

    @Mock
    private WeatherMapper weatherMapper;

    @InjectMocks
    private WeatherService weatherService;


    @Test
    void shouldReturnResponse() {

        OpenWeatherResponse.Main main =
                new OpenWeatherResponse.Main(
                        30.4,
                        32.0,
                        70
                );

        OpenWeatherResponse.Weather weather =
                new OpenWeatherResponse.Weather("cloudy");

        OpenWeatherResponse response =
                new OpenWeatherResponse(
                        "Hyderabad",
                        main,
                        List.of(weather)
                );

        WeatherResponse expectedResponse =
                new WeatherResponse(
                        "Hyderabad",
                        30.4,
                        32.0,
                        70,
                        "cloudy"
                );

        when(weatherClient.getWeather(any(WeatherQuery.class)))
                .thenReturn(response);

        when(weatherMapper.toResponse(response))
                .thenReturn(expectedResponse);

        WeatherResponse actual =
                weatherService.getWeather("Hyderabad");

        assertEquals(expectedResponse, actual);

        ArgumentCaptor<WeatherQuery> captor =
                ArgumentCaptor.forClass(WeatherQuery.class);

        verify(weatherClient)
                .getWeather(captor.capture());

        WeatherQuery actualQuery =
                captor.getValue();

        assertEquals("Hyderabad", actualQuery.q());
        assertEquals("metric", actualQuery.units());

        verify(weatherMapper)
                .toResponse(response);
    }


    @Test
    void shouldPropagateWeatherException() {

        WeatherException exception =
                new WeatherException(
                        404,
                        "City not found"
                );

        when(weatherClient.getWeather(any(WeatherQuery.class)))
                .thenThrow(exception);

        WeatherException actualException =
                assertThrows(
                        WeatherException.class,
                        () -> weatherService.getWeather("Hyderabad")
                );

        assertEquals(404, actualException.getStatus());
        assertEquals(
                "City not found",
                actualException.getMessage()
        );

        verify(weatherClient)
                .getWeather(any(WeatherQuery.class));

        verify(weatherMapper, never())
                .toResponse(any(OpenWeatherResponse.class));
    }
}