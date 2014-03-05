package com.tr.service;

import com.tr.mongo.entity.GroupMember;
import com.tr.mongo.entity.GroupMemberFlattened;
import com.tr.mongo.repository.GroupMemberFlattenedRepository;
import com.tr.mongo.repository.GroupMemberRepository;
import com.tr.utils.EntityPaging;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class GroupMemberService {

    @Autowired private GroupMemberRepository groupMemberRepository;
    @Autowired private GroupMemberFlattenedRepository groupMemberFlattenedRepository;

    private static final Logger log = Logger.getLogger(GroupMemberService.class);

    private static int PAGE_SIZE = 50;

    @Nonnull
    public List<GroupMember> findGroupMembers(@Nonnull final String groupDn, @Nonnull GroupMember.TYPE type) {
        return groupMemberRepository.findByOwnerDnFilterByType(groupDn, type);
    }

    public List<GroupMember> findGroupMembers(@Nonnull final String groupDn, @Nonnull GroupMember.TYPE type, @Nonnull EntityPaging entityPaging) {
        Page<GroupMember> groupMembers = groupMemberRepository.findByOwnerDnFilterByType(groupDn, type, entityPaging.getPageable());
        if (groupMembers.hasNextPage()) {
            entityPaging.setPageable(groupMembers.nextPageable());
        } else {
            entityPaging.reset();
        }

        return groupMembers.getContent();
    }

    public int flattenGroupMemberships(@Nonnull final String groupDn) {
        int count = 0;

        Set<String> traversedDns = new HashSet<String>();
        LinkedList<String> nestedGroups = new LinkedList<String>();
        nestedGroups.add(groupDn);

        while (nestedGroups.peek() != null) {
            String groupDnToSearch = nestedGroups.poll();

            if (traversedDns.contains(groupDnToSearch)) {
                continue;
            }

            // search users
            EntityPaging entityPaging = new EntityPaging(PAGE_SIZE);
            do {
                List<GroupMemberFlattened> flatMembers = new ArrayList<GroupMemberFlattened>();
                List<GroupMember> memberList = findGroupMembers(groupDnToSearch, GroupMember.TYPE.USER, entityPaging);

                //log.info("+++++++++++++++++++++++++Start Paging while Flattening+++++++++++++++");

                for (GroupMember member : memberList) {
                    if (groupMemberFlattenedRepository.findByOwnerAndMemberDn(groupDn, member.getMemberDn()) == null) {
                        flatMembers.add(new GroupMemberFlattened(groupDn, member.getMemberDn()));
                        count++;

                        log.info("Adding user: " + member.getMemberDn() + " to group: " + groupDn);
                    }
                }
                groupMemberFlattenedRepository.save(flatMembers);
                //log.info("+++++++++++++++++++++++++End Paging while Flattening+++++++++++++++");
            } while (entityPaging.hasMore());

            // search nested members
            entityPaging.reset();
            do {
                List<GroupMember> memberList = findGroupMembers(groupDnToSearch, GroupMember.TYPE.GROUP, entityPaging);

                for (GroupMember member : memberList) {
                    nestedGroups.add(member.getMemberDn());
                }
            } while (entityPaging.hasMore());

            traversedDns.add(groupDnToSearch);
        }
        return count;
    }

}
