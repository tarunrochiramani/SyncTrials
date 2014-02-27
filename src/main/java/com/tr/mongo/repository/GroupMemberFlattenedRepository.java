package com.tr.mongo.repository;

import com.tr.mongo.entity.GroupMemberFlattened;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupMemberFlattenedRepository extends MongoRepository<GroupMemberFlattened, String> {
    public List<GroupMemberFlattened> findByOwnerDn(String ownerDn);
    public List<GroupMemberFlattened> findByMemberDn(String memberDn);

    @Query(value = "{ 'ownerDn' : ?0, 'memberDn' : ?1 }")
    public GroupMemberFlattened findByOwnerAndMemberDn(String ownerDn, String memberDn);
}
