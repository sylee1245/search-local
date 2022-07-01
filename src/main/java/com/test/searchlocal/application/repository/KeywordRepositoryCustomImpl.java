package com.test.searchlocal.application.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JpaRepository 이외의 정의된 검색 키워드 Repository 구현
 */
@Repository
public class KeywordRepositoryCustomImpl implements KeywordRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    /**
     * 검색 조건 저장 및 count 갱신
     *
     * @param keyword: 검색 조건
     */
    @Override
    public void save(String keyword) {
        // 동일 keyword가 추가 되어 있는지 검사
        List<KeywordEntity> list = em.createQuery("SELECT k FROM KeywordEntity k WHERE k.keyword = :keyword", KeywordEntity.class)
                .setParameter("keyword", keyword)
                .getResultList();
        if (list == null || list.isEmpty()) {
            // 추가
            em.persist(new KeywordEntity(keyword, 1));
        } else {
            // 수정
            KeywordEntity keywordEntity = list.get(0);
            keywordEntity.setCount(keywordEntity.getCount() + 1);
            keywordEntity.setUpdatedTime(LocalDateTime.now());
            em.merge(keywordEntity);
        }
    }

    /**
     * 검색 키워드 Top 목록 조회
     *
     * @param count: 조회할 Top 갯수
     * @return 검색 키워드 Top 목록
     */
    @Override
    public List<KeywordEntity> aggregateTop(int count) {
        return em.createQuery("SELECT k FROM KeywordEntity k ORDER BY k.count DESC, k.updatedTime DESC", KeywordEntity.class)
                .setMaxResults(count)
                .getResultList();
    }
}
