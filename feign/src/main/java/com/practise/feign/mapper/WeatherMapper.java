package com.practise.feign.mapper;

import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.*;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mapping(target = "city", source = "name")
    @Mapping(target = "temperature", source = "main.temp")
    @Mapping(target = "feelsLike", source = "main.feelsLike")
    @Mapping(target = "description", source = "weather")
    @Mapping(target = "humidity", source = "main.humidity")
    WeatherResponse toResponse(OpenWeatherResponse response);

    default String mapDescription(
            List<OpenWeatherResponse.Weather> weather) {

        if (weather == null || weather.isEmpty()) {
            return null;
        }

        return weather.get(0).description();
    }
}