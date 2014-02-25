package com.tr.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

@Component
public class GroupMember {

    public enum TYPE {
        GROUP, USER
    };

    @Id
    private String id;

    private String ownerDn;
    private String memberDn;
    private TYPE type;

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

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
