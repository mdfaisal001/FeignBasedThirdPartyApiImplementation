package com.practise.feign.dto;
import java.util.List;

public record OpenWeatherResponse(
        String name,
        Main main,
        List<Weather> weather
) {

    public record Main(
            double temp,
            double feels_like,
            int humidity
    ) {
    }

    public record Weather(
            String description
    ) {
    }
}