package com.test.searchlocal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 키워드 도메인 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordInfo {
    private String keyword;
    private int count;
}
