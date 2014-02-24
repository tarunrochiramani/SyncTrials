package com.tr.service;

import com.google.common.base.Preconditions;
import com.tr.ldap.LdapService;
import com.tr.mongo.entity.Group;
import com.tr.utils.LdapPaging;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GroupService implements EntityService<Group>  {
    private static final Logger log = Logger.getLogger(GroupService.class);

    @Autowired private LDAPConnectionPool ldapConnectionPool;
    @Autowired private LdapService ldapService;

    @Override
    @Nonnull
    public List<Group> loadFromLdap(@Nonnull final String searchDn, @Nonnull String[] attributesToReturn, @Nullable LdapPaging ldapPaging) {
        Preconditions.checkArgument(StringUtils.isNotBlank(searchDn));

        List<Group> groups = new ArrayList<Group>();
        try {
            LDAPConnection ldapConnection = ldapConnectionPool.getConnection();

            List<Entry> ldapResults = ldapService.searchLdapGroups(ldapConnection, ldapPaging, searchDn, attributesToReturn);

            for (Entry ldapResult : ldapResults) {
                Group groupToAdd = ldapGroupToGroup(ldapResult);
                groupToAdd.setSyncGroup(true);
                groups.add(groupToAdd);
            }

            // Call differ. Differ should return only the list of groups that are being either added/updated

            ldapConnectionPool.releaseConnection(ldapConnection);
        } catch (LDAPException e) {
            log.error(e);
        }

        return groups;
    }


    @Nonnull
    Group ldapGroupToGroup(@Nonnull final Entry ldapGroup) {
        Group groupToAdd = new Group();
        groupToAdd.setDn(ldapGroup.getDN());

        Map<String, List<String>> attributesMap = groupToAdd.getAttributes();
        for (Attribute attribute : ldapGroup.getAttributes()) {
            String attributeName = attribute.getName();

            List<String> attributeValues;
            if (attributeName.equalsIgnoreCase("objectGuid")) {
                byte[] byteArrayValue = attribute.getValueByteArray();
                attributeValues = new ArrayList<String>();
                attributeValues.add(new String(Base64.encodeBase64(byteArrayValue), Charset.forName("UTF-8")));
            } else {
                attributeValues = Arrays.asList(attribute.getValues());
            }

            if (attributeName.equals("member")) {
                groupToAdd.setMemberAttributeHash(getHashValue(attributeValues));
            }

            attributesMap.put(attributeName, attributeValues);
        }

        groupToAdd.setAttributeHash(getHashValue(attributesMap));

        return groupToAdd;
    }

    @Nullable
    String getHashValue(Object object) {
        String retVal = null;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(object.toString().getBytes("UTF-8"));
            byte[] digest = sha.digest();

            retVal = new String(Hex.encodeHex(digest));
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }

        return retVal;
    }
}
