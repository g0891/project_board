package com.example.board.config.feign;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class BankingServiceConfiguration {

    @Primary
    @Bean
    public ErrorDecoder getErrorDecoder() {return new BankingErrorDecoder();}

}
