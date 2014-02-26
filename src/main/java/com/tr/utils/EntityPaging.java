package com.tr.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class EntityPaging {
    private Pageable pageable;
    private int pageSize;
    private boolean hasMore = false;

    public EntityPaging(int pageSize) {
        this.pageSize = pageSize;
        this.pageable = new PageRequest(0, pageSize);
    }

    public int getPageSize() {
        return pageSize;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
        hasMore = true;
    }

    public boolean hasMore() {
        return hasMore;
    }

    public void reset() {
        hasMore = false;
        pageable = new PageRequest(0, pageSize);
    }
}
