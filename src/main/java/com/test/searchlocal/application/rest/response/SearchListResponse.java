package com.test.searchlocal.application.rest.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 검색 서비스 List 응답값
 *
 * @param <E>
 */
@NoArgsConstructor
@Data
public class SearchListResponse<E> {
    int totalCount;
    List<E> list;

    public SearchListResponse(List<E> list) {
        this.totalCount = list.size();
        this.list = list;
    }
}
