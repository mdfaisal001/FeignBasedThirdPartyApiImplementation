package com.practise.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record WeatherResponse (

    String city,
    double temperature,
     double feelsLike,
    int humidity,
    String description ){}
