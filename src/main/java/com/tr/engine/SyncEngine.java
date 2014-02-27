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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SyncEngine {
    private static Logger logger = Logger.getLogger(SyncEngine.class);

    @Autowired private LdapConfig ldapConfig;
    @Autowired private GroupService groupService;
    @Autowired private GroupMemberService groupMemberService;

    public Map<String, Integer> syncGroups() throws GroupLoadingException, GroupMemberLoadingException {
        Map<String, Integer> groupStats = new HashMap<String, Integer>();

        try {
            for (String groupDn: ldapConfig.getGroupDNs()) {
                LdapPaging ldapPaging = new LdapPaging();
                do {
                    List<Group> groupList = groupService.loadDiffAndSave(groupDn, ldapConfig.getGroupAttributes().toArray(new String[]{}), ldapPaging);
                    for (Group group : groupList) {
                        int flattenedMembers = resolveGroupMemberships(group);
                        groupStats.put(group.getDn(), flattenedMembers);
                    }

                } while (ldapPaging != null && ldapPaging.hasMore());
            }
        } catch (GroupLoadingException e) {
            logger.error(e);
            throw e;
        } catch (GroupMemberLoadingException e) {
            logger.error(e);
            throw e;
        }

        return groupStats;
    }

    private int resolveGroupMemberships(Group group) throws GroupMemberLoadingException {
        groupService.resolveGroupMembers(group);
        int flattenedGroupMembers = groupMemberService.flattenGroupMemberships(group.getDn());

        logger.info("Flattened Group Members: " + flattenedGroupMembers + " for group: " + group.getDn());

        return flattenedGroupMembers;
    }
}
