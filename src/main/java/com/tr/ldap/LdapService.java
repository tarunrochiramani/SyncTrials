package com.tr.ldap;

import com.google.common.base.Preconditions;
import com.tr.utils.LdapPaging;
import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.DNEntrySource;
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
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Component
public class LdapService {
    private static Logger logger = Logger.getLogger(LdapService.class);
    private static final int DEFAULT_PAGE_SIZE = 50;

    @Nonnull
    public LDAPConnection createConnection(@Nonnull final LdapConfig ldapConfig) throws LDAPException {
        Preconditions.checkNotNull(ldapConfig);
        Preconditions.checkNotNull(ldapConfig.getHost());
        Preconditions.checkNotNull(ldapConfig.getPort());
        Preconditions.checkNotNull(ldapConfig.getBindDn());
        Preconditions.checkNotNull(ldapConfig.getPassword());

        logger.info("Creating Ldap Connection with - " + ldapConfig);

        LDAPConnection connection;
        connection = new LDAPConnection(ldapConfig.getHost(), ldapConfig.getPort(), ldapConfig.getBindDn(), ldapConfig.getPassword());
        logger.info(connection);

        return connection;
    }

    @Nonnull
    public List<Entry> ldapSearch(@Nonnull final LDAPConnection connection, @Nonnull final SearchRequest searchRequest) throws EntrySourceException, LDAPException {
        return ldapSearch(connection, searchRequest, null);
    }

    @Nonnull
    public List<Entry> ldapSearch(@Nonnull final LDAPConnection connection, @Nonnull final SearchRequest searchRequest, @Nullable LdapPaging ldapPaging) throws LDAPException, EntrySourceException {
        LDAPEntrySource entrySource = null;
        ASN1OctetString cookie = null;
        List<Entry> ldapSearchResults = new ArrayList();
        int pageSize = DEFAULT_PAGE_SIZE;
        boolean getAllResults = true;

        // set up cookie and page size if LdapPaging was passed.
        if (ldapPaging != null) {
            cookie = ldapPaging.getPaginationCookie() != null ? new ASN1OctetString(ldapPaging.getPaginationCookie()) : null;
            pageSize = ldapPaging.getPageSize() > 0 ? ldapPaging.getPageSize() : DEFAULT_PAGE_SIZE;
            getAllResults = false;
        }

        try {
            do {
                searchRequest.setControls(new Control[] { new SimplePagedResultsControl(pageSize, cookie) });
                logger.info("Ldap search using : connection - " + connection + "\nsearchRequest - " + searchRequest);
                entrySource = new LDAPEntrySource(connection, searchRequest, false);
                logger.info("Completed search for " + searchRequest);

                while (true) {
                    Entry entry = entrySource.nextEntry();
                    if (entry == null) {
                        break;
                    }
                    ldapSearchResults.add(entry);
                }

                cookie = null;
                for (Control c : entrySource.getSearchResult().getResponseControls()) {
                    if (c instanceof SimplePagedResultsControl) {
                        cookie = ((SimplePagedResultsControl) c).getCookie();
                    }
                }
            } while ((cookie != null) && (cookie.getValueLength() > 0) && getAllResults == true);
        } catch (LDAPException e) {
            logger.error("Exception in Ldap Entry Source search." , e);
            throw e;
        } catch (EntrySourceException e) {
            logger.error("Exception in Ldap Entry Source search." , e);
            throw e;
        }
        finally {
            entrySource.close();
        }
        return ldapSearchResults;
    }

    @Nonnull
    public List<Entry> searchLdapGroups(@Nonnull final LDAPConnection connection, @Nullable LdapPaging ldapPaging, @Nonnull final String searchDn, @Nonnull String[] attributesToReturn) throws EntrySourceException, LDAPException {
        SearchRequest searchRequest = new SearchRequest(searchDn, SearchScope.SUB, Filter.createEqualityFilter("objectClass", "group"), attributesToReturn);
        return ldapSearch(connection, searchRequest);
    }

    @Nonnull
    public List<Entry> searchLdapGroupMembers(@Nonnull final LDAPConnection connection, @Nonnull final List<String> memberDNs) throws EntrySourceException {
        List<Entry> ldapGroupMembers = new ArrayList<Entry>();

        try {
            //TODO: May be fetch only desired attributes here?
            DNEntrySource entrySource = new DNEntrySource(connection, memberDNs);
            while (true) {
                Entry entry = entrySource.nextEntry();
                if (entry == null) {
                    break;
                }
                ldapGroupMembers.add(entry);
            }
        } catch (EntrySourceException e) {
            logger.error("Error during DNEntrySourceSearch for Group Members." , e);
            throw e;
        }

        return ldapGroupMembers;
    }
}
