package com.tr.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Group {
    @Id
    private String id;

    private String dn;
    private String attributeHash;
    private String memberAttributeHash;
    private boolean syncGroup;
    private Map<String, List<String>> attributes = new HashMap<String, List<String>>();


    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getAttributeHash() {
        return attributeHash;
    }

    public void setAttributeHash(String attributeHash) {
        this.attributeHash = attributeHash;
    }

    public String getMemberAttributeHash() {
        return memberAttributeHash;
    }

    public void setMemberAttributeHash(String memberAttributeHash) {
        this.memberAttributeHash = memberAttributeHash;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public boolean isSyncGroup() {
        return syncGroup;
    }

    public void setSyncGroup(boolean syncGroup) {
        this.syncGroup = syncGroup;
    }

    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || !(object instanceof Group)) {
            return false;
        }

        Group groupToCompare = (Group)object;
        if (this.dn.equals(groupToCompare.getDn()) &&
            this.attributeHash.equals(groupToCompare.getAttributeHash()) &&
            this.memberAttributeHash.equals(groupToCompare.getMemberAttributeHash()) &&
            this.syncGroup == groupToCompare.isSyncGroup()) {
            return true;
        }

        return result;
    }
}
