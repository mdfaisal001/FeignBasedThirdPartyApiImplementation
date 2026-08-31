package com.practise.feign;

import com.practise.feign.client.WeatherClient;
import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherQuery;
import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.exceptions.WeatherException;
import com.practise.feign.mapper.WeatherMapper;
import com.practise.feign.service.WeatherService;

import net.bytebuddy.asm.MemberSubstitution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

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
    static Stream<Arguments> weatherData(){
            return Stream.of(
                    Arguments.of("Hyderabad", 30.4, 32.0, 70, "cloudy"),
                    Arguments.of("Chennai", 31.5, 33.0, 75, "sunny"),
                    Arguments.of("Bangalore", 25.0, 26.0, 65, "rainy")
            );
    }

    @ParameterizedTest
    @MethodSource("weatherData")
    void shouldReturnResponse( String city,
                               double temperature,
                               double feelsLike,
                               int humidity,
                               String description) {

        OpenWeatherResponse.Main main =
                new OpenWeatherResponse.Main(
                        temperature,
                        feelsLike,
                        humidity
                );

        OpenWeatherResponse.Weather weather =
                new OpenWeatherResponse.Weather(description);

        OpenWeatherResponse response =
                new OpenWeatherResponse(
                        city,
                        main,
                        List.of(weather)
                );

        WeatherResponse expectedResponse =
                new WeatherResponse(
                        city,
                        temperature,
                        feelsLike,
                        humidity,
                        description
                );

        when(weatherClient.getWeather(any(WeatherQuery.class)))
                .thenReturn(response);

        when(weatherMapper.toResponse(response))
                .thenReturn(expectedResponse);

        WeatherResponse actual =
                weatherService.getWeather(city);

        assertEquals(expectedResponse, actual);

        ArgumentCaptor<WeatherQuery> captor =
                ArgumentCaptor.forClass(WeatherQuery.class);

        verify(weatherClient)
                .getWeather(captor.capture());

        WeatherQuery actualQuery =
                captor.getValue();

        assertEquals(city, actualQuery.q());
        assertEquals("metric", actualQuery.units());

        verify(weatherMapper)
                .toResponse(response);
    }


    static Stream<Arguments> weatherExceptionData() {
        return Stream.of(
                Arguments.of("Hyderabad", 404, "City not found"),
                Arguments.of("Chennai", 400, "Invalid city"),
                Arguments.of("Bangalore", 500, "Weather service failed")
        );
    }

    @ParameterizedTest
    @MethodSource("weatherExceptionData")
    void shouldPropagateWeatherException(String city, int status, String message) {

        WeatherException exception =
                new WeatherException(
                        status,
                        message
                );

        when(weatherClient.getWeather(any(WeatherQuery.class)))
                .thenThrow(exception);

        WeatherException actualException =
                assertThrows(
                        WeatherException.class,
                        () -> weatherService.getWeather(city)
                );

        assertEquals(status, actualException.getStatus());
        assertEquals(
                message,
                actualException.getMessage()
        );

        verify(weatherClient)
                .getWeather(any(WeatherQuery.class));

        verify(weatherMapper, never())
                .toResponse(any(OpenWeatherResponse.class));
    }
}