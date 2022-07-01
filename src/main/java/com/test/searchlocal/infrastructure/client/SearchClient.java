package com.test.searchlocal.infrastructure.client;

import com.test.searchlocal.domain.model.LocalInfo;

import java.util.List;

public interface SearchClient {
    List<LocalInfo> searchKeyword(String keyword, int page, int size);
}
