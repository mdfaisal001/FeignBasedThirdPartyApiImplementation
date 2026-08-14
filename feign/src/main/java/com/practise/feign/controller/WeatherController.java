package com.practise.feign.controller;

import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping
    public WeatherResponse getWeather(@RequestParam String city){
        return weatherService.getWeather(city);
    }

}
