package com.tr.mongo.repository;

import com.tr.mongo.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
    public Group findByName(String name);
}
