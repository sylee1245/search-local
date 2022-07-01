package com.test.searchlocal.domain.service;

import com.test.searchlocal.application.repository.KeywordRepository;
import com.test.searchlocal.domain.model.KeywordInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 검색 키워드 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final ModelMapper modelMapper;

    /**
     * 키워드 count 저장
     *
     * @param keyword: 키워드
     */
    public void save(String keyword) {
        keywordRepository.save(keyword);
    }

    /**
     * 검색 키워드 Top 목록 조회
     *
     * @param count: 최대 조회 수
     * @return 검색 키워드 Top 목록
     */
    public List<KeywordInfo> searchTopList(int count) {
        return keywordRepository.aggregateTop(count).stream()
                .map(t -> modelMapper.map(t, KeywordInfo.class)).collect(Collectors.toList());
    }
}
