package com.practise.feign;

import com.practise.feign.controller.WeatherController;
import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.exceptions.WeatherException;
import com.practise.feign.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
    @Test
    void shouldTheRequestReachTheServie() throws Exception{

        WeatherResponse weatherResponse = new WeatherResponse("Hyderabad",
                32.0,45.0,70,"cloudy");

        when(weatherService.getWeather("Hyderabad")).thenReturn(weatherResponse);
        mockMvc.perform(get("/api/v1/weather").param("city","Hyderabad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Hyderabad"))
                .andExpect(jsonPath("$.temperature").value(32.0))
                .andExpect(jsonPath("$.feelsLike").value(45.0))
                .andExpect(jsonPath("$.humidity").value(70))
                .andExpect(jsonPath("$.description").value("cloudy"));


      verify(weatherService,times(1)).getWeather("Hyderabad");

    }

    @Test
    void shouldTheRequestReturnTheException() throws Exception{

        when(weatherService.getWeather("Chennai")).thenThrow(new WeatherException(404,"City not found"));

        mockMvc.perform(get("/api/v1/weather").param("city", "Chennai"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("City not found"));


        verify(weatherService).getWeather("Chennai");
    }


    @Test
    void shouldReturnBadRequestWhenCityIsMissing() throws Exception {

        mockMvc.perform(
                        get("/api/v1/weather")
                )
                .andExpect(status().isBadRequest());
    }


}
