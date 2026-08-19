package com.practise.feign.service;

import com.practise.feign.client.WeatherClient;
import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherQuery;
import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.exceptions.WeatherException;
import com.practise.feign.mapper.WeatherMapper;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;


@Service
public class WeatherService {
    private final WeatherClient weatherClient;
    private final WeatherMapper weatherMapper;

    public WeatherService(WeatherClient weatherClient, WeatherMapper weatherMapper){
        this.weatherClient = weatherClient;
        this.weatherMapper = weatherMapper;
    }
    private final AtomicInteger requestCount = new AtomicInteger();
    public WeatherResponse getWeather(String city){

        int count = requestCount.incrementAndGet();
        System.out.println(
                "Feign request #" + count +
                        " | city=" + city
        );
        WeatherQuery query = new WeatherQuery(city,"metric");


        try {
            OpenWeatherResponse response =
                    weatherClient.getWeather(query);
            System.out.println(response.name());
            return weatherMapper.toResponse(response);

        } catch (WeatherException e) {

            System.out.println("STATUS: " + e.getStatus());
            System.out.println("MESSAGE: " + e.getMessage());

            throw e;
        }
    }
}

