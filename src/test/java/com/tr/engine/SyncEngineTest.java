package com.tr.engine;

import com.tr.AppConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class SyncEngineTest {

    @Autowired private SyncEngine syncEngine;


    @Test
    public void canSync() throws Exception {
        syncEngine.syncGroups();
    }
}
