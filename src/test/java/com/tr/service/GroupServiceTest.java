package com.tr.service;

import com.tr.AppConfiguration;
import com.tr.ldap.LdapConfig;
import com.tr.mongo.entity.Group;
import com.tr.utils.LdapPaging;
import com.unboundid.ldap.sdk.Attribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static com.tr.testutils.builders.SearchResultEntryBuilder.aSearchResultEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class GroupServiceTest {

    @Autowired private GroupService groupService;
    @Autowired private LdapConfig ldapConfig;

    private final Attribute[] attributes = new Attribute[] {new Attribute("member", "CN=testuser1 user1,OU=NestedTesting,DC=hs,DC=trcint,DC=com",
            "CN=NestedTestGroup3,OU=NestedTesting,DC=hs,DC=trcint,DC=com", "CN=NestedTestGroup2,OU=NestedTesting,DC=hs,DC=trcint,DC=com"),
            new Attribute("objectGUID", "98+zzyN2QU6rC8ZMfTC74Q"), new Attribute("canonicalName", "hs.trcint.com/NestedTesting/NestedTestGroup1")};
    private static final String DN = "CN=NestedTestGroup1,OU=NestedTesting,DC=hs,DC=trcint,DC=com";


    @Test
    public void canGetHashValue() {
        String hashValue = groupService.getHashValue("blah");
        assertNotNull(hashValue);
    }

    @Test
    public void canChangeLdapGroupToGroup() {
        Group group = groupService.ldapGroupToGroup(aSearchResultEntry().withDn(DN).withAttributes(attributes).build());

        validateGroup(group);
        assertEquals(DN, group.getDn());
    }

    @Test
    public void canLoadFromLdap() {
        List<Group> groups = new ArrayList<Group>();
        for (String groupDn : ldapConfig.getGroupDNs()) {
            LdapPaging ldapPaging = new LdapPaging();
            do {
                groups.addAll(groupService.loadFromLdap(groupDn, ldapConfig.getGroupAttributes().toArray(new String[]{}), ldapPaging));
            } while (ldapPaging.getPaginationCookie() != null && ldapPaging.getPaginationCookie().length > 0);
        }

        assertFalse(groups.isEmpty());
        assertTrue(groups.size() > 1);
        validateGroup(groups.toArray(new Group[]{}));
    }

    private void validateGroup(Group... groups) {
        assertNotNull(groups);
        for (Group groupToVerify : groups) {
            assertFalse(groupToVerify.getAttributes().isEmpty());
            assertNotNull(groupToVerify.getDn());
            assertNotNull(groupToVerify.getAttributeHash());
            assertNotNull(groupToVerify.getMemberAttributeHash());
        }
    }



}
