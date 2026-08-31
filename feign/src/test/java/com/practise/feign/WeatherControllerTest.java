package com.practise.feign;

import com.practise.feign.controller.WeatherController;
import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.exceptions.WeatherException;
import com.practise.feign.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    static Stream <Arguments> weatherData(){
        return Stream.of(
                Arguments.of("Hyderabad",32.0,45.0,50,"cloudy"),
                Arguments.of("kerala",23.0,59.0,30,"Heavy rain"),
                Arguments.of("Hyderabad",39.0,75.0,100,"Heat waves")
        );
    }

   @ParameterizedTest
   @MethodSource("weatherData")
    void shouldTheRequestReachTheServie(String city, double temperature,
                                        double feelsLike, int humidity, String description) throws Exception{

        WeatherResponse weatherResponse = new WeatherResponse(city,temperature,feelsLike,humidity,description);

        when(weatherService.getWeather(city)).thenReturn(weatherResponse);
        mockMvc.perform(get("/api/v1/weather").param("city",city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value(city))
                .andExpect(jsonPath("$.temperature").value(temperature))
                .andExpect(jsonPath("$.feelsLike").value(feelsLike))
                .andExpect(jsonPath("$.humidity").value(humidity))
                .andExpect(jsonPath("$.description").value(description));


      verify(weatherService,times(1)).getWeather(city);

    }


    static Stream<Arguments> exceptionData(){
        return Stream.of(
                Arguments.of("madras",404,"city name was renamed"),
                Arguments.of("bang",404,"city not found"),
                Arguments.of("odissi",404,"Incorrect city name")
        );
    }
   @ParameterizedTest
   @MethodSource("exceptionData")
    void shouldTheRequestReturnTheException(String city, int status, String message) throws Exception{

        when(weatherService.getWeather(city)).thenThrow(new WeatherException(status,message));

        mockMvc.perform(get("/api/v1/weather").param("city", city))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(status))
                .andExpect(jsonPath("$.message").value(message));


        verify(weatherService).getWeather(city);
    }


    @Test
    void shouldReturnBadRequestWhenCityIsMissing() throws Exception {

        mockMvc.perform(
                        get("/api/v1/weather")
                )
                .andExpect(status().isBadRequest());
    }


}
