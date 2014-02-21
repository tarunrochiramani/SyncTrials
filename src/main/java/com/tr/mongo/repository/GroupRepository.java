package com.tr.mongo.repository;

import com.tr.mongo.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface GroupRepository extends MongoRepository<Group, String> {
    public Group findByDn(String dn);
    public Group findByObjectGuid(String objectGuid);
}
