package com.tr.ldap;

import com.google.common.base.Preconditions;

import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.EntrySourceException;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPEntrySource;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.controls.SimplePagedResultsControl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Component
public class LdapService {
    private static Logger logger = Logger.getLogger(LdapService.class);

    @Nonnull
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

    @Nonnull
    public List<Entry> ldapSearch(@Nonnull final LDAPConnection connection, @Nonnull final SearchRequest searchRequest) {
        LDAPEntrySource entrySource = null;
        ASN1OctetString cookie = null;
        List<Entry> ldapSearchResults = new ArrayList();

        try {
            do {
                searchRequest.setControls(new Control[] { new SimplePagedResultsControl(200, cookie) });
                logger.info("Ldap search using : connection - " + connection + "\nsearchRequest - " + searchRequest);
                entrySource = new LDAPEntrySource(connection, searchRequest, false);
                logger.info("Completed search for " + searchRequest);

                while (true) {
                    Entry entry = entrySource.nextEntry();
                    if (entry == null) {
                        break;
                    }

                    logger.info("Found: " + entry);
                    ldapSearchResults.add(entry);
                }

                cookie = null;
                for (Control c : entrySource.getSearchResult().getResponseControls())
                {
                    if (c instanceof SimplePagedResultsControl)
                    {
                        cookie = ((SimplePagedResultsControl) c).getCookie();
                    }
                }

            } while ((cookie != null) && (cookie.getValueLength() > 0));
        } catch (LDAPException e) {
            logger.error("Exception in Ldap Entry Source search." , e);
        } catch (EntrySourceException e) {
            logger.error("Exception in Ldap Entry Source search." , e);
        }
        finally {
            entrySource.close();
        }

        return ldapSearchResults;
    }


    @Nonnull
    public List<Entry> searchLdapGroups(@Nonnull final LDAPConnection connection, @Nonnull final String searchDn, @Nonnull String[] attributesToReturn) {
        SearchRequest searchRequest = new SearchRequest(searchDn, SearchScope.SUB, Filter.createEqualityFilter("objectClass", "group"), attributesToReturn);
        return ldapSearch(connection, searchRequest);
    }
}
