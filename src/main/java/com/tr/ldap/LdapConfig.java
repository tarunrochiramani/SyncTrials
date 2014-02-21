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

    @Value("#{'${group.dn}'.trim().split(';')}")
    private List<String> groupDNs;

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

    public List<String> getGroupDNs() {
        return groupDNs;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nhost: " + host + "\nport: " + port + "\nbindDn: " + bindDn + "\npassword: " + password);
        stringBuilder.append("\ngroupDns - ");
        for (String groupDn : getGroupDNs()) {
            stringBuilder.append("\n" + groupDn);
        }
        return stringBuilder.toString();
    }
}
