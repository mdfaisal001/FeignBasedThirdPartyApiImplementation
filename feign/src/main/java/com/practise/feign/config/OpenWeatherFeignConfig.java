package com.practise.feign.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class OpenWeatherFeignConfig {

    @Bean
    public RequestInterceptor openWeatherApiKeyInterceptor(@Value("${openweather.api-key}")String apikey){

       return requestTemplate -> requestTemplate.query("appId", apikey);

    }
}
