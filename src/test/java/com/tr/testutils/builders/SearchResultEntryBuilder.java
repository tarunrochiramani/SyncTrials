package com.tr.testutils.builders;

import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.SearchResultEntry;
import org.apache.commons.lang3.builder.Builder;

public class SearchResultEntryBuilder implements Builder<SearchResultEntry> {
    private String dn = "dn";
    private Attribute[] attributes = new Attribute[0];
    private Control[] control = new Control[0];

    public static SearchResultEntryBuilder aSearchResultEntry() {
        return new SearchResultEntryBuilder();
    }

    public SearchResultEntryBuilder withDn(String dn) {
        this.dn = dn;
        return this;
    }

    public SearchResultEntryBuilder withAttributes(Attribute... attributes) {
        this.attributes = new Attribute[attributes.length];
        this.attributes = attributes;
        return this;
    }

    public SearchResultEntryBuilder withControls(Control... control) {
        this.control = new Control[control.length];
        this.control = control;
        return this;
    }

    @Override
    public SearchResultEntry build() {
        return new SearchResultEntry(dn, attributes, control);
    }
}
