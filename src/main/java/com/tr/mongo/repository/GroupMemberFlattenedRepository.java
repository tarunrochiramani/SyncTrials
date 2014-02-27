package com.tr.mongo.repository;

import com.tr.mongo.entity.GroupMemberFlattened;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupMemberFlattenedRepository extends MongoRepository<GroupMemberFlattened, String> {
    public List<GroupMemberFlattened> findByOwnerDn(String ownerDn);
    public List<GroupMemberFlattened> findByMemberDn(String memberDn);
}
