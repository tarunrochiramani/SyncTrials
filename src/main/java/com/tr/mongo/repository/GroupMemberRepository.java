package com.tr.mongo.repository;

import com.tr.mongo.entity.GroupMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupMemberRepository extends MongoRepository <GroupMember, String> {

    public List<GroupMember> findByOwnerDn(String ownerDn);
    public Page<GroupMember> findByOwnerDn(String ownerDn, Pageable pageable);

    public List<GroupMember> findByMemberDn(String memberDn);
    public Page<GroupMember> findByMemberDn(String memberDn, Pageable pageable);

    public List<GroupMember> findByType(GroupMember.TYPE type);
    public Page<GroupMember> findByType(GroupMember.TYPE type, Pageable pageable);

    @Query (value = "{ 'ownerDn' : ?0, 'type' : ?1 }")
    public List<GroupMember> findByOwnerDnFilterByType(String ownerDn, GroupMember.TYPE type);

    @Query (value = "{ 'ownerDn' : ?0, 'type' : ?1 }")
    public Page<GroupMember> findByOwnerDnFilterByType(String ownerDn, GroupMember.TYPE type, Pageable pageable);

    @Query(value = "{ 'ownerDn' : ?0, 'memberDn' : ?1 }")
    public GroupMember findByOwnerAndMemberDn(String ownerDn, String memberDn);

}
