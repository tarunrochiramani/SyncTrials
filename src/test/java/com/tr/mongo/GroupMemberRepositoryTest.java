package com.tr.mongo;

import com.tr.AppConfiguration;
import com.tr.mongo.entity.GroupMember;
import com.tr.mongo.repository.GroupMemberRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.tr.testutils.builders.GroupMemberBuilder.aGroupMember;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class GroupMemberRepositoryTest {

    @Autowired private GroupMemberRepository repository;

    private GroupMember groupMember = aGroupMember().build();

    @Before
    public void setUp() {
        repository.deleteAll();
        repository.save(groupMember);
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void canFindByOwnerDn() {
        List<GroupMember> groupMembers = repository.findByOwnerDn(groupMember.getOwnerDn());

        assertNotNull(groupMembers);
        assertEquals(1, groupMembers.size());
        assertEquals(groupMember.getOwnerDn(), groupMembers.get(0).getOwnerDn());
    }

    @Test
    public void canFindByMemberDn() {
        List<GroupMember> groupMembers = repository.findByMemberDn(groupMember.getMemberDn());

        assertNotNull(groupMembers);
        assertEquals(1, groupMembers.size());
        assertEquals(groupMember.getMemberDn(), groupMembers.get(0).getMemberDn());
    }

    @Test
    public void canFindByType() {
        List<GroupMember> groupMembers = repository.findByType(groupMember.getType());

        assertNotNull(groupMembers);
        assertEquals(1, groupMembers.size());
        assertEquals(groupMember.getType(), groupMembers.get(0).getType());
    }

    @Test
    public void canFindByOwnerDnWithPaging() {
        int totalSize = 30;
        addGroupMembersToDB(groupMember.getOwnerDn(), null, GroupMember.TYPE.USER, totalSize);

        int page = 0;
        int size = 10;

        Pageable pageable = new PageRequest(page, size);

        while (true) {
            Page<GroupMember> result = repository.findByOwnerDn(groupMember.getOwnerDn(), pageable);

            for (GroupMember groupMemberRetrieved : result.getContent()) {
                assertEquals(groupMember.getOwnerDn(), groupMemberRetrieved.getOwnerDn());
                assertNotNull(groupMemberRetrieved.getMemberDn());
                assertEquals(GroupMember.TYPE.USER, groupMemberRetrieved.getType());
            }


            if (!result.hasNextPage()) {
                break;
            }

            page++;
            pageable = result.nextPageable();
        }

        assertEquals(page, totalSize/size);
    }


    @Test
    public void canFindByOwnerDnFilterByType() {
        int size = 5;
        repository.deleteAll();
        addGroupMembersToDB(groupMember.getOwnerDn(), null, GroupMember.TYPE.USER, size);
        addGroupMembersToDB(null, groupMember.getMemberDn(), GroupMember.TYPE.USER, size);

        List<GroupMember> members = repository.findByOwnerDnFilterByType(groupMember.getOwnerDn(), GroupMember.TYPE.USER);

        assertNotNull(members);
        assertEquals(size, members.size());
    }

    @Test
    public void canFindByOwnerDnFilterByTypeWithPaging() {
        int size = 10;
        int pageSize = 5;
        repository.deleteAll();
        addGroupMembersToDB(groupMember.getOwnerDn(), null, GroupMember.TYPE.USER, size);
        addGroupMembersToDB(null, groupMember.getMemberDn(), GroupMember.TYPE.USER, size);

        Pageable pageable = new PageRequest(0, pageSize);
        List<GroupMember> members = new ArrayList<GroupMember>();

        while (true){
            Page<GroupMember> memberResults = repository.findByOwnerDnFilterByType(groupMember.getOwnerDn(), GroupMember.TYPE.USER, pageable);
            pageable = memberResults.nextPageable();
            members.addAll(memberResults.getContent());

            if (!memberResults.hasNextPage()) {
                break;
            }
        };

        assertNotNull(members);
        assertEquals(size, members.size());
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
