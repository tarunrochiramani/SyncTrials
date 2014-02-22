package com.tr;

import com.tr.ldap.LdapConfig;
import com.tr.mongo.entity.Group;
import com.tr.mongo.repository.GroupRepository;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class AppConfigurationTest {
    private static Logger logger = Logger.getLogger(AppConfigurationTest.class);

    @Autowired private LdapConfig ldapConfig;
    @Autowired private LDAPConnectionPool ldapConnectionPool;
    @Autowired private GroupRepository groupRepository;

    @Test
    public void canSetUpLdapConfig() {
        assertNotNull(ldapConfig);
        assertNotNull(ldapConfig.getHost());
        assertNotNull(ldapConfig.getPort());
        assertNotNull(ldapConfig.getBindDn());
        assertNotNull(ldapConfig.getPassword());
        assertNotNull(ldapConfig.getGroupDNs());
        assertFalse(ldapConfig.getGroupDNs().isEmpty());
        assertFalse(ldapConfig.getGroupAttributes().isEmpty());
        logger.warn(ldapConfig);
    }

    @Test
    public void canCreateAndReleaseConnectionFromConnectionPool() throws LDAPException {
        assertNotNull(ldapConnectionPool);

        LDAPConnection ldapConnection = ldapConnectionPool.getConnection();
        assertNotNull(ldapConnection);

        ldapConnectionPool.releaseConnection(ldapConnection);
    }

    @Test
    public void canCleanOutAllGroups() {
        groupRepository.deleteAll();
        List<Group> groupList = groupRepository.findAll();
        assertNotNull(groupList);
        assertTrue(groupList.isEmpty());
    }
}
