package com.tr.engine;

import com.tr.exception.GroupLoadingException;
import com.tr.exception.GroupMemberLoadingException;
import com.tr.ldap.LdapConfig;
import com.tr.mongo.entity.Group;
import com.tr.service.GroupMemberService;
import com.tr.service.GroupService;
import com.tr.utils.LdapPaging;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SyncEngine {
    private static Logger logger = Logger.getLogger(SyncEngine.class);

    @Autowired private LdapConfig ldapConfig;
    @Autowired private GroupService groupService;
    @Autowired private GroupMemberService groupMemberService;

    public void syncGroups() throws GroupLoadingException, GroupMemberLoadingException {
        try {
            for (String groupDn: ldapConfig.getGroupDNs()) {
                LdapPaging ldapPaging = new LdapPaging();
                do {
                    List<Group> groupList = groupService.loadDiffAndSave(groupDn, ldapConfig.getGroupAttributes().toArray(new String[]{}), ldapPaging);
                    resolveGroupMemberships(groupList);
                } while (ldapPaging != null && ldapPaging.hasMore());
            }
        } catch (GroupLoadingException e) {
            logger.error(e);
            throw e;
        } catch (GroupMemberLoadingException e) {
            logger.error(e);
            throw e;
        }
    }

    private void resolveGroupMemberships(List<Group> groupList) throws GroupMemberLoadingException {
        for (Group group : groupList) {
            groupService.resolveGroupMembers(group);
            groupMemberService.flattenGroupMemberships(group.getDn());
        }
    }
}
