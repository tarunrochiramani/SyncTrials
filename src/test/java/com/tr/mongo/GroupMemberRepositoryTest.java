package com.tr.mongo;

import com.tr.AppConfiguration;
import com.tr.mongo.entity.GroupMember;
import com.tr.mongo.repository.GroupMemberRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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
}
