package com.example.demo.config;

import com.example.demo.Car;
import org.springframework.context.annotation.Bean;

public interface BeanA {

    @Bean
    default Car createACar() {
        return new Car("ACar");
    }
}
