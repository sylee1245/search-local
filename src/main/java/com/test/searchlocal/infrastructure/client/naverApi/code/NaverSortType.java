package com.test.searchlocal.infrastructure.client.naverApi.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * NAVER 정렬 옵션
 */
@Getter
@AllArgsConstructor
public enum NaverSortType {
    RANDOM("random"),
    COMMENT("comment");

    private final String code;
}