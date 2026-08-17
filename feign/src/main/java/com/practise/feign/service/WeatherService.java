package com.practise.feign.service;

import com.practise.feign.client.WeatherClient;
import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherQuery;
import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.exceptions.WeatherException;
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
        WeatherQuery query = new WeatherQuery(city,"metric");
        try {
            OpenWeatherResponse response =
                    weatherClient.getWeather(query);

            return weatherMapper.toResponse(response);

        } catch (WeatherException e) {

            System.out.println("STATUS: " + e.getStatus());
            System.out.println("MESSAGE: " + e.getMessage());

            throw e;
        }
    }
}

