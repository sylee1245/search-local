package com.test.searchlocal.domain.service;

import com.test.searchlocal.application.repository.KeywordEntity;
import com.test.searchlocal.application.repository.KeywordRepository;
import com.test.searchlocal.domain.model.KeywordInfo;
import com.test.searchlocal.domain.model.LocalInfo;
import com.test.searchlocal.domain.service.KeywordService;
import com.test.searchlocal.domain.service.SearchLocalService;
import com.test.searchlocal.infrastructure.client.kakaoApi.KakaoClient;
import com.test.searchlocal.infrastructure.client.naverApi.NaverClient;
import com.test.searchlocal.infrastructure.config.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest
public class KeywordServiceTest {
    @Autowired
    KeywordService keywordService;
    @Autowired
    KeywordRepository keywordRepository;
    @Autowired
    ModelMapper modelMapper;

    @Test
    @DisplayName("1) 키워드 count 저장")
    void whenCallSave_thenInsertOrUpdateCount() {
        String keyword = "곱창";
        KeywordEntity keywordEntity = new KeywordEntity();
        Example<KeywordEntity> example = Example.of(keywordEntity);
        Optional<KeywordEntity> before = keywordRepository.findOne(example);
        // 1. 이전에 추가된 키워드가 없을 때 1로 추가 되는지 확인
        if (before.isEmpty()) {
            keywordService.save(keyword);
            Optional<KeywordEntity> after = keywordRepository.findAll().stream().filter(k -> k.getKeyword().equals(keyword)).findAny();
            assertEquals(after.get().getCount(),1);
        }
        // 2. 이전에 추가된 키워드가 있을 때 count 1 증가되는지 확인
        before = keywordRepository.findOne(example);
        if (!before.isEmpty()) {
            keywordService.save(keyword);
            Optional<KeywordEntity> after = keywordRepository.findAll().stream().filter(k -> k.getKeyword().equals(keyword)).findAny();
            assertEquals(after.get().getCount(), before.get().getCount()+1);
        }
    }

    @Test
    @DisplayName("2) 검색 키워드 Top 목록 조회 ")
    void whenSearchTopList_thenReturnTopList() {
        IntStream.range(1, 10).forEach(i -> {
            for (int j = 0; j < i; j++) {
                keywordService.save("keyword"+i);
            }
        });
        List<KeywordInfo> serviceWillReturn = keywordRepository.findAll().stream()
                .sorted((k1, k2) -> {
                    int compareCount = k2.getCount() - k1.getCount();
                    if (compareCount != 0) {
                        return compareCount;
                    }
                    return k2.getUpdatedTime().compareTo(k1.getUpdatedTime());
                })
                .limit(Constants.AGGREGATE_TOP_COUNT)
                .map(k -> modelMapper.map(k, KeywordInfo.class))
                .collect(Collectors.toList());
        List<KeywordInfo> serviceReturn = keywordService.searchTopList(Constants.AGGREGATE_TOP_COUNT);

        assertTrue(serviceReturn.size()==serviceWillReturn.size());
        for (int i = 0; i < serviceReturn.size(); i++) {
            assertEquals(serviceReturn.get(i).getKeyword(), serviceWillReturn.get(i).getKeyword());
            assertEquals(serviceReturn.get(i).getCount(), serviceWillReturn.get(i).getCount());
        }
    }

}
