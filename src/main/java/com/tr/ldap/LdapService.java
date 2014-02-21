package com.tr.ldap;

import com.google.common.base.Preconditions;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.annotation.Nonnull;

@Component
public class LdapService {
    private static Logger logger = Logger.getLogger(LdapService.class);

    public LDAPConnection createConnection(@Nonnull final LdapConfig ldapConfig) {
        Preconditions.checkNotNull(ldapConfig);
        Preconditions.checkNotNull(ldapConfig.getHost());
        Preconditions.checkNotNull(ldapConfig.getPort());
        Preconditions.checkNotNull(ldapConfig.getBindDn());
        Preconditions.checkNotNull(ldapConfig.getPassword());

        logger.info("Creating Ldap Connection with - " + ldapConfig);

        LDAPConnection connection = null;
        try {
            connection = new LDAPConnection(ldapConfig.getHost(), ldapConfig.getPort(), ldapConfig.getBindDn(), ldapConfig.getPassword());
            logger.info(connection);
        } catch (LDAPException e) {
            logger.info("Error during creating Ldap Connection." ,e);
        }
        return connection;
    }
}
