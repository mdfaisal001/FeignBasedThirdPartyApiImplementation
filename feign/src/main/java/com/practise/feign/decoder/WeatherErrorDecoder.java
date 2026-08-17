package com.practise.feign.decoder;

import com.practise.feign.dto.WeatherErrorResponse;
import com.practise.feign.exceptions.WeatherException;
import feign.Response;
import feign.codec.ErrorDecoder;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class WeatherErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;
    public WeatherErrorDecoder(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    @Override
    public Exception decode(String methodKey, Response response){
       try{
           if(response.body() != null){

               WeatherErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream()
                       ,WeatherErrorResponse.class);

                return new WeatherException(response.status(),errorResponse.message());
           }

       }
       catch(IOException e){
           return new WeatherException(response.status(), "Failed to read the api response");
       }

       return new WeatherException(response.status(),"Weather api request failed");
    }

}
