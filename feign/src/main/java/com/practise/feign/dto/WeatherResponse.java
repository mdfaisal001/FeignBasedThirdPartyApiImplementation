package com.practise.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {

    private String city;
    private double temperature;
    private double feelsLike;
    private int humidity;
    private String description;




}
