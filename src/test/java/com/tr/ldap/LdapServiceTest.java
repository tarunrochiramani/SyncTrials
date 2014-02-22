package com.tr.ldap;

import com.tr.AppConfiguration;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchScope;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class LdapServiceTest {

    private static Logger logger = Logger.getLogger(LdapServiceTest.class);

    @Autowired private LDAPConnectionPool ldapConnectionPool;
    @Autowired private LdapConfig ldapConfig;
    @Autowired private LdapService ldapService;

    private LDAPConnection ldapConnection;

    @Before
    public void getLdapConnection() throws LDAPException {
        ldapConnection = ldapConnectionPool.getConnection();
        assertNotNull(ldapConnection);
    }

    @After
    public void releaseLdapConnection() {
        ldapConnectionPool.releaseConnection(ldapConnection);
    }

    @Test
    public void canLdapSearch() {
        SearchRequest searchRequest = new SearchRequest(ldapConfig.getGroupDNs().get(0), SearchScope.SUB, Filter.createPresenceFilter("objectClass"));
        List<Entry> entries = ldapService.ldapSearch(ldapConnection, searchRequest);

        assertNotNull(entries);
        assertEquals(1, entries.size());
        assertEquals(ldapConfig.getGroupDNs().get(0), entries.get(0).getDN());
    }

    @Test
    public void canSearchLdapGroups() {
        List<Entry> entries = ldapService.searchLdapGroups(ldapConnection, ldapConfig.getGroupDNs().get(0), ldapConfig.getGroupAttributes().toArray(new String[]{}));

        assertNotNull(entries);
        assertEquals(ldapConfig.getGroupDNs().get(0), entries.get(0).getDN());
        assertNotNull(entries.get(0).getAttributes());
        logger.info("Attributes returned: " + entries.get(0).getAttributes());
    }
}
