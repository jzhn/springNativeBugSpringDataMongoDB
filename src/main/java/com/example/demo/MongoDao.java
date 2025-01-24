package com.example.demo;

import org.springframework.stereotype.Repository;

@Repository
public class MongoDao implements Dao {
    @Override
    public String getData() {
        return "hello from mongodb";
    }
}
