package com.test.searchlocal.application.repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 검색 키워드 Repository 인터페이스 (JpaRepository 이외의 정의)
 */
public interface KeywordRepositoryCustom {
    /**
     * 검색 조건 저장 및 count 갱신
     *
     * @param keyword: 검색 조건
     */
    @Transactional
    void save(String keyword);

    /**
     * 검색 키워드 Top 목록 조회
     *
     * @param count: 조회할 Top 갯수
     * @return 검색 키워드 Top 목록
     */
    List<KeywordEntity> aggregateTop(int count);
}
