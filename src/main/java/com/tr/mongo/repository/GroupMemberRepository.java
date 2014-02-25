package com.tr.mongo.repository;

import com.tr.mongo.entity.GroupMember;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupMemberRepository extends MongoRepository <GroupMember, String> {

    public List<GroupMember> findByOwnerDn(String ownerDn);
    public List<GroupMember> findByMemberDn(String memberDn);
    public List<GroupMember> findByType(GroupMember.TYPE type);

}
