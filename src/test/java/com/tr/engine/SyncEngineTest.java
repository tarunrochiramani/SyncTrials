package com.tr.engine;

import com.tr.AppConfiguration;
import com.tr.ldap.LdapConfig;
import com.tr.ldap.LdapService;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.EntrySourceException;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchScope;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class SyncEngineTest {

    @Autowired private SyncEngine syncEngine;
    @Autowired private LdapService ldapService;
    @Autowired private LDAPConnectionPool ldapConnectionPool;
    @Autowired private LdapConfig ldapConfig;

    @Test
    public void canSync() throws Exception {
        Map<String, Integer> groupStats = syncEngine.syncGroups();

        assertNotNull(groupStats);

        for (Map.Entry<String, Integer> mapEntry : groupStats.entrySet()) {
            String groupDn = mapEntry.getKey();
            Integer countOfMembers = mapEntry.getValue();

            List<Entry> entries = queryLdapForNumberOfGroupMembers(groupDn);
            assertNotNull(entries);
            assertEquals(entries.size(), countOfMembers.intValue());
        }
    }

    private List<Entry> queryLdapForNumberOfGroupMembers(String groupDN) throws LDAPException, EntrySourceException, IOException {
        LDAPConnection ldapConnection = ldapConnectionPool.getConnection();
        Filter userGroupFilterWithMatchingSearchRule = Filter.createANDFilter(Filter.createEqualityFilter("objectClass", "user"), Filter.create("memberOf:1.2.840.113556.1.4.1941:=" + groupDN));

        SearchRequest searchRequest = new SearchRequest(ldapConfig.getBaseDn(), SearchScope.SUB, userGroupFilterWithMatchingSearchRule);
        return ldapService.ldapSearch(ldapConnection, searchRequest);
    }


}
