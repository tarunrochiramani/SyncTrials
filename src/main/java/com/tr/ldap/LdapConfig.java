package com.tr.ldap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LdapConfig {
    @Value("${ad.host}")
    private String host;

    @Value("${ad.port}")
    private Integer port;

    @Value("${ad.bindDn}")
    private String bindDn;

    @Value("${ad.password}")
    private String password;

    @Value("${ad.baseDn}")
    private String baseDn;

    @Value("#{'${group.dn}'.split(';')}")
    private List<String> groupDNs;

    @Value("#{'${group.attributes}'.split(';')}")
    private List<String> groupAttributes;

    public String getHost() {
        return host;
    }

    public Integer getPort(){
        return port;
    }

    public String getBindDn() {
        return bindDn;
    }

    public String getPassword() {
        return password;
    }

    public String getBaseDn() {
        return baseDn;
    }

    public List<String> getGroupDNs() {
        return groupDNs;
    }

    public List<String> getGroupAttributes() {
        return groupAttributes;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nhost: " + host + "\nport: " + port + "\nbindDn: " + bindDn + "\npassword: " + password);
        stringBuilder.append("\ngroupDns - ");
        for (String groupDn : getGroupDNs()) {
            stringBuilder.append("\n" + groupDn);
        }
        stringBuilder.append("\ngroupAttributes - ");
        for (String attribute : getGroupAttributes()) {
            stringBuilder.append("\n" + attribute);
        }
        return stringBuilder.toString();
    }
}
