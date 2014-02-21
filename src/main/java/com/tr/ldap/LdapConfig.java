package com.tr.ldap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String toString() {
        return "\nhost: " + host + "\nport: " + port + "\nbindDn: " + bindDn + "\npassword: " + password;
    }
}
