package com.practise.feign.client;

import com.practise.feign.dto.OpenWeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "openweather",
        url = "${openweather.base-url}"
)
public interface WeatherClient {

    @GetMapping("/data/2.5/weather")
    OpenWeatherResponse getWeather(
            @RequestParam("q") String city,
            @RequestParam("units") String units,
            @RequestParam("appid") String apiKey
    );
}