package com.example.demo.config;

import com.example.demo.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig implements BeanA {

    // If a bean is defined in an interface and added to a @Configuration class,
    // the processAot does not generate the necessary information to run it natively.

    // Would work fine, but competitions of beans through interface are very nice pattern ;)
//    @Bean
//    public Car createACar() {
//        return new Car("ACar");
//    }

}
