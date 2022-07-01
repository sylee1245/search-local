package com.test.searchlocal.domain.service;

import com.test.searchlocal.domain.model.LocalInfo;
import com.test.searchlocal.infrastructure.client.kakaoApi.KakaoClient;
import com.test.searchlocal.infrastructure.client.naverApi.NaverClient;
import com.test.searchlocal.infrastructure.config.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 장소 검색 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchLocalService {
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;

    /**
     * 장소 검색
     *
     * @param keyword: 검색 키워드
     * @return 각 검색 API 에서 최대 5개씩, 총 10개의 키워드 관련 장소를 검색한 결과
     */
    public List<LocalInfo> search(String keyword) {
        // 카카오 에서 5개 검색
        List<LocalInfo> result = new ArrayList<>(kakaoClient.searchKeyword(keyword, 1, Constants.SEARCH_SIZE));

        // 카카오 에서 5개 모두 검색 안된 경우
        if (result.size() < Constants.SEARCH_SIZE) {
            // 네이버에서 추가 검색 후 결과에 없는 값만 추가
            result.addAll(naverClient.searchKeyword(keyword, 1, Constants.SEARCH_SIZE * 2 - result.size()).stream()
                    .filter(n -> result.stream().noneMatch(r -> isSameLocal(n, r)))
                    .collect(Collectors.toList()));
            return result;
        }

        // 카카오에서 5개 모두 검색된 경우 네이버에서 5개 검색
        List<LocalInfo> naverList = naverClient.searchKeyword(keyword, 1, Constants.SEARCH_SIZE);

        // 네이버 에서 5개 모두 검색 안된 경우
        if (naverList.size() < Constants.SEARCH_SIZE) {
            // 카카오에서 추가 검색 후 부족한 수 만큼 추가
            List<LocalInfo> kakaoList = kakaoClient.searchKeyword(keyword, 2, Constants.SEARCH_SIZE);
            int loopCnt = Math.min(Constants.SEARCH_SIZE - naverList.size(), kakaoList.size());
            for (int i = 0; i < loopCnt; i++) {
                result.add(kakaoList.get(i));
            }
        }

        // 네이버에서 추가 검색 후 결과에 없는 값만 추가
        result.addAll(naverList.stream()
                .filter(n -> result.stream().noneMatch(r -> isSameLocal(n, r)))
                .collect(Collectors.toList()));

        return result;
    }

    /**
     * 두 장소의 일치 여부 판단
     *
     * @param local1: 장소1
     * @param local2: 장소2
     * @return 두 장소의 일치 여부
     */
    private boolean isSameLocal(LocalInfo local1, LocalInfo local2) {
        if (isSameName(local1.getName(), local2.getName())) {
            return true;
        }
        if (StringUtils.isNotBlank(local1.getRoadAddress()) && StringUtils.isNotBlank(local2.getRoadAddress())
                && isSameAddress(local1.getRoadAddress(), local2.getRoadAddress())) {
            return true;
        }
        return StringUtils.isNotBlank(local1.getAddress()) && StringUtils.isNotBlank(local2.getAddress())
                && isSameAddress(local1.getAddress(), local2.getAddress());
    }

    /**
     * 두 장소의 이름 일치 여부 판단
     * - 공백 제거, 태그 제거 후 동일 비교
     *
     * @param name1: 장소1의 이름
     * @param name2: 장소2의 이름
     * @return 일치 여부
     */
    private boolean isSameName(String name1, String name2) {
        name1 = name1.replaceAll("\\p{Z}", "").replaceAll("<[^>]*>", "");
        name2 = name2.replaceAll("\\p{Z}", "").replaceAll("<[^>]*>", "");
        return name1.equals(name2);
    }

    /**
     * 두 장소의 주소 일치 여부 판단
     * - 공백 기준으로 앞 두 글자 일치 여부 비교(특수문자 또는 숫자가 포함된 경우 단어로 비교)
     *
     * @param address1: 장소1의 주소
     * @param address2: 장소2의 주소
     * @return 일치 여부
     */
    private boolean isSameAddress(String address1, String address2) {
        Set<String> addressSet1 = addressConvertToSet(address1.strip());
        Set<String> addressSet2 = addressConvertToSet(address2.strip());
        // 작은 set 기준으로 검사
        if (addressSet1.size() < addressSet2.size()) {
            return addressSet1.stream().allMatch(addressSet2::remove);
        }
        return addressSet2.stream().allMatch(addressSet1::remove);
    }

    /**
     * 주소를 규칙에 맞게 Set으로 변환
     *
     * @param address: 주소
     * @return 공백 기준으로 단어를 구분하여 다음 규칙에 맞게 Set으로 반환
     * - 특수문자: 포함시 단어 전체 반환
     * - 숫자: 포함시 단어 전체 반환
     * - 문자: 앞에서 부터 2글자만 반환
     * ex. 부산광역시 중구 남포동2가 25-10 뉴남포빌딩 3층 -> {부산, 중구, 남포, 25-10, 뉴남, 3층}
     */
    private Set<String> addressConvertToSet(String address) {
        return Stream.of(address.split(" ")).map(a -> {
            // 특수 문자 포함인 경우
            if (!a.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) {
                return a;
            }
            // 숫자가 포함된 경우
            char[] aArr = a.toCharArray();
            for (char c : aArr) {
                if (Character.isDigit(c)) {
                    return a;
                }
            }
            // 문자 경우
            return a.length() > 2 ? a.substring(0, 2) : a;
        }).collect(Collectors.toSet());
    }
}
