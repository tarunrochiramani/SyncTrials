package com.tr.mongo;

import com.tr.AppConfiguration;
import com.tr.mongo.entity.Group;
import com.tr.mongo.repository.GroupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class GroupRepositoryTest {

    @Autowired private GroupRepository repository;
    private static final String GROUP_NAME = "sampleGroup";
    private Group group;

    @Before
    public void cleanUp() {
        repository.deleteAll();
        group = new Group();
        group.setName(GROUP_NAME);
        repository.save(group);
    }

    @Test
    public void canFindAll() {
        List<Group> groupList = repository.findAll();
        assertNotNull(groupList);
        assertFalse(groupList.isEmpty());
        assertEquals(1, groupList.size());
        assertEquals(groupList.get(0).getName(), group.getName());
    }

    @Test
    public void canFindByName() {
        Group groupRetrieved = repository.findByName(GROUP_NAME);

        assertNotNull(groupRetrieved);
        assertEquals(group.getName(), groupRetrieved.getName());
    }
}
