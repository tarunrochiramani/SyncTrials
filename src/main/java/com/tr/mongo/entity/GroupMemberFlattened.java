package com.tr.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

@Component
public class GroupMemberFlattened {

    private String ownerDn;
    private String memberDn;

    @Id
    private String id;

    public GroupMemberFlattened() {}

    public GroupMemberFlattened(String ownerDn, String memberDn) {
        this.ownerDn = ownerDn;
        this.memberDn = memberDn;
    }

    public String getOwnerDn() {
        return ownerDn;
    }

    public void setOwnerDn(String ownerDn) {
        this.ownerDn = ownerDn;
    }

    public String getMemberDn() {
        return memberDn;
    }

    public void setMemberDn(String memberDn) {
        this.memberDn = memberDn;
    }
}
