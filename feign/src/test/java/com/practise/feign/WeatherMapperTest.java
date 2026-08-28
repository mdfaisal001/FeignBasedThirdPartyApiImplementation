package com.practise.feign;

import aQute.bnd.annotation.jpms.Open;
import com.practise.feign.dto.OpenWeatherResponse;
import com.practise.feign.dto.WeatherResponse;
import com.practise.feign.mapper.WeatherMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherMapperTest {

    @Test
    void shouldMapperWorks(){

        WeatherMapper weatherMapper = Mappers.getMapper(WeatherMapper.class);
        OpenWeatherResponse.Main main = new OpenWeatherResponse.Main(32.0,45.0,70);
        OpenWeatherResponse.Weather weather = new OpenWeatherResponse.Weather("Heavy rain");
        OpenWeatherResponse response = new OpenWeatherResponse("Hyderabad",main, List.of(weather));

        WeatherResponse actual = weatherMapper.toResponse(response);

        assertAll(
                ()-> assertEquals(32.0,actual.temperature()),
                ()-> assertEquals(45.0,actual.feelsLike()),
                ()-> assertEquals(70,actual.humidity()),
                ()-> assertEquals("Heavy rain",actual.description())
        );
    }


    @Test
    void shouldReturnNullDescriptionWhenWeatherIsNull() {

        WeatherMapper weatherMapper =
                Mappers.getMapper(WeatherMapper.class);

        OpenWeatherResponse.Main main =
                new OpenWeatherResponse.Main(
                        32.0,
                        45.0,
                        70
                );

        OpenWeatherResponse response =
                new OpenWeatherResponse(
                        "Hyderabad",
                        main, null
                );

        WeatherResponse actual =
                weatherMapper.toResponse(response);

        assertNull(actual.description());
    }


    @Test
    void shouldReturnNullDescriptionWhenWeatherIsEmpty() {

        WeatherMapper weatherMapper = Mappers.getMapper(WeatherMapper.class);

        OpenWeatherResponse.Main main = new OpenWeatherResponse.Main(22.0,34.0,54);


        OpenWeatherResponse response = new OpenWeatherResponse("Hyderabad",main,List.of());

        WeatherResponse actual = weatherMapper.toResponse(response);

        assertNull(actual.description());
    }
}

