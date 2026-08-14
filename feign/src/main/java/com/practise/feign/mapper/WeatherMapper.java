package com.practise.feign.mapper;

import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    @Mapping(target = "city", source = "name")
    @Mapping(target = "temperature", source = "main.temp")
    @Mapping(target = "feelsLike", source = "main.feels_like")
    @Mapping(target = "description", source = "weather")
    WeatherResponse toResponse(OpenWeatherResponse response);

    default String mapDescription(
            java.util.List<OpenWeatherResponse.Weather> weather) {

        if (weather == null || weather.isEmpty()) {
            return null;
        }

        return weather.get(0).getDescription();
    }
}