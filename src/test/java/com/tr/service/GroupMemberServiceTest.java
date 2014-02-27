package com.tr.service;

import com.tr.AppConfiguration;
import com.tr.mongo.entity.Group;
import com.tr.mongo.entity.GroupMember;
import com.tr.mongo.repository.GroupMemberFlattenedRepository;
import com.tr.mongo.repository.GroupMemberRepository;
import com.tr.utils.EntityPaging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.tr.testutils.builders.GroupBuilder.aGroup;
import static com.tr.testutils.builders.GroupMemberBuilder.aGroupMember;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class GroupMemberServiceTest {

    @Autowired private GroupMemberRepository groupMemberRepository;
    @Autowired private GroupMemberService groupMemberService;
    @Autowired private GroupMemberFlattenedRepository groupMemberFlattenedRepository;

    private Group group = aGroup().build();

    @Before
    @After
    public void cleanUp() {
        groupMemberRepository.deleteAll();
        groupMemberFlattenedRepository.deleteAll();
    }

    @Test
    public void canLoadWithPagination() throws Exception {
        int totalSize = 20;
        int pageSize = 10;
        addGroupMembersToDB(group.getDn(), null, GroupMember.TYPE.USER, totalSize);
        EntityPaging entityPaging = new EntityPaging(pageSize);

        int count = 0;
        List<GroupMember> groupMemberList = new ArrayList<GroupMember>();
        do {
            groupMemberList.addAll(groupMemberService.findGroupMembers(group.getDn(), GroupMember.TYPE.USER, entityPaging));
            count ++;
        } while (entityPaging.hasMore());

        assertEquals(totalSize/pageSize, count);
        assertEquals(totalSize, groupMemberList.size());
    }

    @Test
    public void canFlattenGroupMemberships() {
        addGroupMembersToDB(group.getDn(), "NestedGroup", GroupMember.TYPE.GROUP, 1);
        addGroupMembersToDB("NestedGroup", "NestedGroup1", GroupMember.TYPE.GROUP, 1);
        addGroupMembersToDB("NestedGroup", "NestedGroup_User1", GroupMember.TYPE.USER, 1);
        addGroupMembersToDB("NestedGroup1", "NestedGroup1_User2", GroupMember.TYPE.USER, 1);
        addGroupMembersToDB("NestedGroup1", "NestedGroup", GroupMember.TYPE.GROUP, 1);

        int count = groupMemberService.flattenGroupMemberships(group.getDn());

        assertEquals(count, groupMemberFlattenedRepository.findAll().size());
    }

    private void addGroupMembersToDB(String ownerDn, String memberDn, GroupMember.TYPE type, int iterations) {
        Random random = new Random();
        for (int count = 0; count<iterations; count++) {
            String randomString = Long.toString(random.nextLong());
            String owner = ownerDn != null? ownerDn : randomString;
            String member = memberDn != null? memberDn : randomString;

            groupMemberRepository.save(aGroupMember().withOwnerDn(owner).withMemberDn(member).withType(type).build());
        }
    }
}
