package com.tr.service;

import com.tr.AppConfiguration;
import com.tr.mongo.entity.Group;
import com.tr.mongo.entity.GroupMember;
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
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class GroupMemberServiceTest {

    @Autowired private GroupMemberRepository repository;
    @Autowired private GroupMemberService groupMemberService;

    private Group group = aGroup().build();

    @Before
    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void canLoadWithoutPagination() throws Exception {
        int size = 20;
        addGroupMembersToDB(group.getDn(), null, GroupMember.TYPE.USER, size);

        List<GroupMember> memberList = groupMemberService.findGroupMembers(group, GroupMember.TYPE.USER);

        assertNotNull(memberList);
        assertEquals(size, memberList.size());
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
            groupMemberList.addAll(groupMemberService.findGroupMembers(group, GroupMember.TYPE.USER, entityPaging));
            count ++;
        } while (entityPaging.hasMore());

        assertEquals(totalSize/pageSize, count);
        assertEquals(totalSize, groupMemberList.size());
    }

    private void addGroupMembersToDB(String ownerDn, String memberDn, GroupMember.TYPE type, int iterations) {
        Random random = new Random();
        for (int count = 0; count<iterations; count++) {
            String randomString = Long.toString(random.nextLong());
            String owner = ownerDn != null? ownerDn : randomString;
            String member = memberDn != null? memberDn : randomString;

            repository.save(aGroupMember().withOwnerDn(owner).withMemberDn(member).withType(type).build());
        }
    }
}
