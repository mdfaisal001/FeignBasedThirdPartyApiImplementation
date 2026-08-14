package com.practise.feign.service;

import com.practise.feign.client.WeatherClient;
import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.mapper.WeatherMapper;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {
    private final WeatherClient weatherClient;
    private final WeatherMapper weatherMapper;
    @Value("${openweather.api-key}")
    String apiKey;
    public WeatherService(WeatherClient weatherClient, WeatherMapper weatherMapper){
        this.weatherClient = weatherClient;
        this.weatherMapper = weatherMapper;
    }

    public WeatherResponse getWeather(String city){
        OpenWeatherResponse response =weatherClient.getWeather(
                city,
                "metric",
                apiKey
        );

        return weatherMapper.toResponse(response);
    }
}

/*package com.practise.feign.service;

import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.mapper.WeatherMapper;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {
    private final RestClient restClient;
    private final WeatherMapper weatherMapper;
    @Value("${openweather.api-key}")
    String apiKey;
    public WeatherService(RestClient restClient, WeatherMapper weatherMapper){
        this.restClient = restClient;
        this.weatherMapper = weatherMapper;
    }

    public WeatherResponse getWeather(String city){
        OpenWeatherResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/weather")
                        .queryParam("q", city)
                        .queryParam("units", "metric")
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .body(OpenWeatherResponse.class);

        return weatherMapper.toResponse(response);
    }
}
*/