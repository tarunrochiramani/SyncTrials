package com.tr.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

@Component
public class Group {
    @Id
    private String id;

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
