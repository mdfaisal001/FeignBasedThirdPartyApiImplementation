package com.practise.feign.service;

import com.practise.feign.client.WeatherClient;
import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.mapper.WeatherMapper;

import org.springframework.stereotype.Service;


@Service
public class WeatherService {
    private final WeatherClient weatherClient;
    private final WeatherMapper weatherMapper;

    public WeatherService(WeatherClient weatherClient, WeatherMapper weatherMapper){
        this.weatherClient = weatherClient;
        this.weatherMapper = weatherMapper;
    }

    public WeatherResponse getWeather(String city){
        OpenWeatherResponse response =weatherClient.getWeather(
                city,
                "metric"
        );
        return weatherMapper.toResponse(response);
    }
}

