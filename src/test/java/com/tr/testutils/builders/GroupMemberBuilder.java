package com.tr.testutils.builders;

import com.tr.mongo.entity.GroupMember;
import org.apache.commons.lang3.builder.Builder;
import org.springframework.data.annotation.Id;

public class GroupMemberBuilder implements Builder<GroupMember> {
    @Id
    private String id;

    private String ownerDn = "CN=TarunGroup,CN=Users,DC=hs,DC=trcint,DC=com";
    private String memberDn = "CN=Tarun Rochiramani,CN=Users,DC=hs,DC=trcint,DC=com";
    private GroupMember.TYPE type = GroupMember.TYPE.USER;

    public static GroupMemberBuilder aGroupMember() {
        return new GroupMemberBuilder();
    }

    public GroupMemberBuilder withOwnerDn(String ownerDn) {
        this.ownerDn = ownerDn;
        return this;
    }

    public GroupMemberBuilder withMemberDn(String memberDn) {
        this.memberDn = memberDn;
        return this;
    }

    public GroupMemberBuilder withType(GroupMember.TYPE type) {
        this.type = type;
        return this;
    }


    @Override
    public GroupMember build() {
        GroupMember groupMember = new GroupMember();
        groupMember.setOwnerDn(ownerDn);
        groupMember.setMemberDn(memberDn);
        groupMember.setType(type);

        return groupMember;
    }
}
