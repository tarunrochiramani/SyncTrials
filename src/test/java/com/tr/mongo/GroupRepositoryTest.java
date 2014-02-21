package com.tr.mongo;

import com.tr.AppConfiguration;
import com.tr.mongo.entity.Group;
import com.tr.mongo.repository.GroupRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.tr.testutils.builders.GroupBuilder.aGroup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class GroupRepositoryTest {

    @Autowired private GroupRepository repository;    ;
    private Group group = aGroup().build();

    @Before
    public void setUp() {
        repository.save(group);
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void canFindAll() {
        List<Group> groupList = repository.findAll();
        assertNotNull(groupList);
        assertFalse(groupList.isEmpty());
        assertEquals(1, groupList.size());
        assertEquals(groupList.get(0), group);
    }

    @Test
    public void canFindByDn() {
        Group groupRetrieved = repository.findByDn(group.getDn());

        assertNotNull(groupRetrieved);
        assertEquals(group, groupRetrieved);
    }

    @Test
    public void canFindByObjectGuid() {
        Group groupRetrieved = repository.findByObjectGuid(group.getObjectGuid());

        assertNotNull(groupRetrieved);
        assertEquals(group, groupRetrieved);
    }
}
