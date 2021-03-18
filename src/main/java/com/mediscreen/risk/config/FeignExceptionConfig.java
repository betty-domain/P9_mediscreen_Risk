package com.mediscreen.risk.config;

import com.mediscreen.risk.exceptions.FeignErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

    @Bean
    public FeignErrorDecoder getCustomErrorDecoder()
    {
        return  new FeignErrorDecoder();
    }
}
