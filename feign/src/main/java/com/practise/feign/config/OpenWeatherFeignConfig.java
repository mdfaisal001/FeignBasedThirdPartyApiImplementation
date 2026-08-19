package com.practise.feign.config;

import com.practise.feign.decoder.WeatherErrorDecoder;

import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

public class OpenWeatherFeignConfig {

    @Bean
    public RequestInterceptor openWeatherApiKeyInterceptor(@Value("${openweather.api-key}")String apikey){

       return requestTemplate -> requestTemplate.query("appId", apikey);

    }
    @Bean
    public ErrorDecoder weatherErrorDecoder(ObjectMapper objectMapper){
        return new WeatherErrorDecoder(objectMapper);

    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                1000,
                5000,
                3
        );
    }

}
