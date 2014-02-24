package com.tr.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LdapPaging {
    private int pageSize;
    private ByteArrayOutputStream pagedResultsCookieByteArray = new ByteArrayOutputStream();


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void resetPagedResultsCookieByteArray() {
        if (pagedResultsCookieByteArray != null) {
            pagedResultsCookieByteArray.reset();
        } else {
            pagedResultsCookieByteArray = new ByteArrayOutputStream();
        }
    }

    public byte[] getPaginationCookie() {
        byte[] cookie = pagedResultsCookieByteArray.size() > 0 ? pagedResultsCookieByteArray.toByteArray() : null;
        return cookie;
    }

    public void setPaginationCookie(byte[] cookie) throws IOException {
        resetPagedResultsCookieByteArray();
        if (cookie != null) {
            pagedResultsCookieByteArray.write(cookie);
        }
    }
}
