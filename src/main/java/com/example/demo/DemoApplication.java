package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@Configuration
@Import({BeanConfig.class})
public class DemoApplication {

    public DemoApplication(Dao dao) {
        System.out.println("Dao.getData: " + dao.getData());
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
