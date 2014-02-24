package com.tr.service;

import com.tr.utils.LdapPaging;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface EntityService<T> {
    List<T> loadFromLdap(@Nonnull final String searchDn, @Nonnull String[] attributesToReturn, @Nullable LdapPaging ldapPaging);
}
