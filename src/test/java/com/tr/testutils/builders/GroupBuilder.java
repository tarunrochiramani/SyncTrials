package com.tr.testutils.builders;

import com.tr.mongo.entity.Group;
import org.apache.commons.lang3.builder.Builder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class GroupBuilder implements Builder<Group> {

    public Random random = new Random();
    private String dn = "cn=sampleGroup,dc=tr,dc=com";
    private String objectGuid = UUID.randomUUID().toString();
    private String attributeHash = Long.toString(random.nextLong());
    private String memberAttributeHash = Long.toString(random.nextLong());
    private boolean syncGroup = true;
    private Map<String, List<String>> attributes = new HashMap<String, List<String>>();

    public static GroupBuilder aGroup() {
        return new GroupBuilder();
    }

    public GroupBuilder withDn(String dn) {
        this.dn = dn;
        return this;
    }

    public GroupBuilder withObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
        return this;
    }

    public GroupBuilder withAttributeHash(String attributeHash) {
        this.attributeHash = attributeHash;
        return this;
    }

    public GroupBuilder withMemberAttributeHash(String memberAttributeHash) {
        this.memberAttributeHash = memberAttributeHash;
        return this;
    }

    public GroupBuilder isSyncGroup(boolean syncGroup) {
        this.syncGroup = syncGroup;
        return this;
    }

    @Override
    public Group build() {
        Group group = new Group();
        group.setDn(dn);
        group.setObjectGuid(objectGuid);
        group.setAttributeHash(attributeHash);
        group.setMemberAttributeHash(memberAttributeHash);
        group.setSyncGroup(syncGroup);
        return group;
    }
}
