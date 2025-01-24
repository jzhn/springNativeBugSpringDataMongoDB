package com.example.demo;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class BeanConfig {

    @Bean
    public Dao myDao() {
        return new MongoDao();
    }
    
    // replace above with below to workaround the issue
    // since defining bean with concreate type would 
    // make process-aot generate the required cglib proxy  
    // @Bean
    // public MongoDao myDao() {
    //     return new MongoDao();
    // }
}
