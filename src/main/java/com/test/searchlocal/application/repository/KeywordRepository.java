package com.test.searchlocal.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 검색 키워드 Repository 인터페이스
 */
public interface KeywordRepository extends JpaRepository<KeywordEntity, Long>, KeywordRepositoryCustom {
}
