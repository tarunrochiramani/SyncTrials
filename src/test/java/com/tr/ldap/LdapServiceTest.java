package com.tr.ldap;

import com.tr.AppConfiguration;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchScope;
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

    @Autowired private LdapConfig ldapConfig;
    @Autowired private LdapService ldapService;

    private LDAPConnection ldapConnection;
    @Before
    public void createLdapConnection() {
        ldapConnection = ldapService.createConnection(ldapConfig);
        assertNotNull(ldapConnection);
        assertNotNull(ldapConfig);
    }

    @Test
    public void canLdapSearch() {
        SearchRequest searchRequest = new SearchRequest(ldapConfig.getGroupDNs().get(0), SearchScope.SUB, Filter.createPresenceFilter("objectClass"));
        List<Entry> entries = ldapService.ldapSearch(ldapConnection, searchRequest);

        assertNotNull(entries);
        assertEquals(1, entries.size());
        assertEquals(ldapConfig.getGroupDNs().get(0), entries.get(0).getDN());
    }
}
