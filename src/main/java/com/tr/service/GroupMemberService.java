package com.tr.service;

import com.tr.exception.GroupMemberLoadingException;
import com.tr.mongo.entity.Group;
import com.tr.mongo.entity.GroupMember;
import com.tr.mongo.repository.GroupMemberRepository;
import com.tr.utils.EntityPaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

@Component
public class GroupMemberService {

    @Autowired private GroupService groupService;
    @Autowired private GroupMemberRepository groupMemberRepository;

    @Nonnull
    public List<GroupMember> findGroupMembers(@Nonnull final Group group, @Nonnull GroupMember.TYPE type) throws GroupMemberLoadingException {
        resolveMembers(group);
        return groupMemberRepository.findByType(type);
    }

    public List<GroupMember> findGroupMembers(@Nonnull final Group group, @Nonnull GroupMember.TYPE type, @Nonnull EntityPaging entityPaging) throws GroupMemberLoadingException {
        resolveMembers(group);

        Page<GroupMember> groupMembers = groupMemberRepository.findByType(type, entityPaging.getPageable());
        if (groupMembers.hasNextPage()) {
            entityPaging.setPageable(groupMembers.nextPageable());
        } else {
            entityPaging.reset();
        }

        return groupMembers.getContent();
    }

    private void resolveMembers(Group group) throws GroupMemberLoadingException {
        if (groupMemberRepository.findByOwnerDn(group.getDn()).isEmpty()) {
            groupService.resolveGroupMembers(group);
        }
    }

}
