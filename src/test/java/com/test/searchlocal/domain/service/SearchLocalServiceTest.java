package com.test.searchlocal.domain.service;

import com.test.searchlocal.domain.model.LocalInfo;
import com.test.searchlocal.domain.service.SearchLocalService;
import com.test.searchlocal.infrastructure.client.kakaoApi.KakaoClient;
import com.test.searchlocal.infrastructure.client.naverApi.NaverClient;
import com.test.searchlocal.infrastructure.config.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest
public class SearchLocalServiceTest {
    @Autowired
    private SearchLocalService searchLocalService;
    @MockBean
    private KakaoClient kakaoClient;
    @MockBean
    private NaverClient naverClient;

    @Test
    @DisplayName("1-1) 장소 검색 - 카카오 5개, 네이버 5개 검색된 경우")
    void givenAllSearchSize_whenCallSearch_thenReturnList() {
        // 1. 장소가 모두 다르게 검색된 경우
        final String keyword = "곱창";
        List<LocalInfo> kakaoWillReturn = IntStream.range(1,Constants.SEARCH_SIZE+1)
                .mapToObj(i -> LocalInfo.builder().name("곱창"+i).build()).collect(Collectors.toList());
        List<LocalInfo> naverWillReturn = IntStream.range(1,Constants.SEARCH_SIZE+1)
                .mapToObj(i -> LocalInfo.builder().name("곱창 집"+i).build()).collect(Collectors.toList());

        given(kakaoClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE)).willReturn(kakaoWillReturn);
        given(naverClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE)).willReturn(naverWillReturn);

        List<String> serviceWillReturnName = List.of("곱창1","곱창2","곱창3","곱창4","곱창5","곱창 집1","곱창 집2","곱창 집3","곱창 집4","곱창 집5");
        List<LocalInfo> serviceReturn = searchLocalService.search(keyword);

        assertList(serviceReturn, serviceWillReturnName);

        // 2. 장소가 모두 일부 같게 검색된 경우
        naverWillReturn.get(0).setName("곱창1");
        given(naverClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE)).willReturn(naverWillReturn);

        serviceWillReturnName = List.of("곱창1","곱창2","곱창3","곱창4","곱창5","곱창 집2","곱창 집3","곱창 집4","곱창 집5");
        serviceReturn = searchLocalService.search(keyword);

        assertList(serviceReturn, serviceWillReturnName);
    }

    @Test
    @DisplayName("1-2) 장소 검색 - 카카오 5개, 네이버 3개 검색된 경우")
    void givenOnlyKakaoSearchSize_whenCallSearch_thenReturnList() {
        // 1. 장소가 모두 다르게 검색된 경우
        final String keyword = "곱창";
        List<LocalInfo> kakaoWillReturn = IntStream.range(1,Constants.SEARCH_SIZE+1)
                .mapToObj(i -> LocalInfo.builder().name("곱창"+i).build()).collect(Collectors.toList());
        List<LocalInfo> naverWillReturn = IntStream.range(1,4)
                .mapToObj(i -> LocalInfo.builder().name("곱창 집"+i).build()).collect(Collectors.toList());

        given(kakaoClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE)).willReturn(kakaoWillReturn);
        given(naverClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE)).willReturn(naverWillReturn);

        List<String> serviceWillReturnName = List.of("곱창1","곱창2","곱창3","곱창4","곱창5","곱창 집1","곱창 집2","곱창 집3");
        List<LocalInfo> serviceReturn = searchLocalService.search(keyword);

        assertTrue(serviceReturn.size()==serviceWillReturnName.size());
        for (int i = 0; i < serviceReturn.size(); i++)
            assertEquals(serviceReturn.get(i).getName(), serviceWillReturnName.get(i));

        // 2. 장소가 모두 일부 같게 검색된 경우
        naverWillReturn.get(0).setName("곱창1");
        given(naverClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE)).willReturn(naverWillReturn);

        serviceWillReturnName = List.of("곱창1","곱창2","곱창3","곱창4","곱창5","곱창 집2","곱창 집3");
        serviceReturn = searchLocalService.search(keyword);

        assertList(serviceReturn, serviceWillReturnName);
    }

    @Test
    @DisplayName("1-3) 장소 검색 - 카카오 3개, 네이버 5개 검색된 경우")
    void givenOnlyNaverSearchSize_whenCallSearch_thenReturnList() {
        // 1. 장소가 모두 다르게 검색된 경우
        final String keyword = "곱창";
        List<LocalInfo> kakaoWillReturn = IntStream.range(1,4)
                .mapToObj(i -> LocalInfo.builder().name("곱창"+i).build()).collect(Collectors.toList());
        List<LocalInfo> naverWillReturn = IntStream.range(1,Constants.SEARCH_SIZE*2-2)
                .mapToObj(i -> LocalInfo.builder().name("곱창 집"+i).build()).collect(Collectors.toList());
        given(kakaoClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE)).willReturn(kakaoWillReturn);
        given(naverClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE*2-3)).willReturn(naverWillReturn);

        List<String> serviceWillReturnName = List.of("곱창1","곱창2","곱창3","곱창 집1","곱창 집2","곱창 집3","곱창 집4","곱창 집5","곱창 집6","곱창 집7");
        List<LocalInfo> serviceReturn = searchLocalService.search(keyword);

        assertList(serviceReturn, serviceWillReturnName);

        // 2. 장소가 모두 일부 같게 검색된 경우
        naverWillReturn.get(0).setName("곱창1");
        given(naverClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE)).willReturn(naverWillReturn);

        serviceWillReturnName = List.of("곱창1","곱창2","곱창3","곱창 집2","곱창 집3","곱창 집4","곱창 집5","곱창 집6","곱창 집7");
        serviceReturn = searchLocalService.search(keyword);

        assertList(serviceReturn, serviceWillReturnName);
    }

    @Test
    @DisplayName("1-4) 장소 검색 - 카카오 3개, 네이버 3개 검색된 경우")
    void givenNeitherSearchSize_whenCallSearch_thenReturnList() {
        // 1. 장소가 모두 다르게 검색된 경우
        final String keyword = "곱창";
        List<LocalInfo> kakaoWillReturn = IntStream.range(1,4)
                .mapToObj(i -> LocalInfo.builder().name("곱창"+i).build()).collect(Collectors.toList());
        List<LocalInfo> naverWillReturn = IntStream.range(1,4)
                .mapToObj(i -> LocalInfo.builder().name("곱창 집"+i).build()).collect(Collectors.toList());
        given(kakaoClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE)).willReturn(kakaoWillReturn);
        given(naverClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE*2-3)).willReturn(naverWillReturn);

        List<String> serviceWillReturnName = List.of("곱창1","곱창2","곱창3","곱창 집1","곱창 집2","곱창 집3");
        List<LocalInfo> serviceReturn = searchLocalService.search(keyword);

        assertList(serviceReturn, serviceWillReturnName);

        // 2. 장소가 모두 일부 같게 검색된 경우
        naverWillReturn.get(0).setName("곱창1");
        given(naverClient.searchKeyword("곱창", 1, Constants.SEARCH_SIZE)).willReturn(naverWillReturn);

        serviceWillReturnName = List.of("곱창1","곱창2","곱창3","곱창 집2","곱창 집3");
        serviceReturn = searchLocalService.search(keyword);

        assertList(serviceReturn, serviceWillReturnName);
    }

    private void assertList(List<LocalInfo> serviceReturn, List<String> serviceWillReturnName) {
        assertTrue(serviceReturn.size()==serviceWillReturnName.size());
        for (int i = 0; i < serviceReturn.size(); i++)
            assertEquals(serviceReturn.get(i).getName(), serviceWillReturnName.get(i));
    }

    @Test
    @DisplayName("2) 두 장소의 일치 여부 판단")
    void whenCallIsSamleLocal_thenReturnBoolean() {
        // 1. 이름이 같은 경우
        LocalInfo localInfo1 = LocalInfo.builder().name("곱창1").address("곱창1 주소").roadAddress("곱창1 도로명 주소").build();
        LocalInfo localInfo2 = LocalInfo.builder().name("곱창1").address("곱창2 주소").roadAddress("곱창2 도로명 주소").build();
        assertEquals(ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"isSameLocal", localInfo1, localInfo2), Boolean.TRUE);

        // 2. 주소가 같은 경우
        localInfo1 = LocalInfo.builder().name("곱창1").address("곱창1 주소").roadAddress("곱창1 도로명 주소").build();
        localInfo2 = LocalInfo.builder().name("곱창2").address("곱창1 주소").roadAddress("곱창2 도로명 주소").build();
        assertEquals(ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"isSameLocal", localInfo1, localInfo2), Boolean.TRUE);

        // 3. 도로명 주소가 같은 경우
        localInfo1 = LocalInfo.builder().name("곱창1").address("곱창1 주소").roadAddress("곱창1 도로명 주소").build();
        localInfo2 = LocalInfo.builder().name("곱창2").address("곱창2 주소").roadAddress("곱창1 도로명 주소").build();
        assertEquals(ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"isSameLocal", localInfo1, localInfo2), Boolean.TRUE);

        // 4. 이름, 주소, 도로명 주소 모두 같지 않은 경우
        localInfo1 = LocalInfo.builder().name("곱창1").address("곱창1 주소").roadAddress("곱창1 도로명 주소").build();
        localInfo2 = LocalInfo.builder().name("곱창2").address("곱창2 주소").roadAddress("곱창2 도로명 주소").build();
        assertEquals(ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"isSameLocal", localInfo1, localInfo2), Boolean.FALSE);
    }

    @Test
    @DisplayName("3) 두 장소의 이름 일치 여부 판단")
    void whenCallIsSamleName_thenReturnBoolean() {
        // 1. 공백 제거 후 동일 문자인 경우
        String name1 = "곱창집";
        String name2 = "곱창 집";
        assertEquals(ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"isSameName", name1, name2), Boolean.TRUE);

        // 2. HTML 테그 제거 후 동일 문자인 경우
        name1 = "곱창집";
        name2 = "<b>곱창집</b>";
        assertEquals(ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"isSameName", name1, name2), Boolean.TRUE);

        // 3. 공백 제거, 테그 제거 후 동일 문자가 아닌 경우
        name1 = "곱창";
        name2 = "<b>곱창 집</b>";
        assertEquals(ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"isSameName", name1, name2), Boolean.FALSE);
    }

    @Test
    @DisplayName("4) 두 장소의 주소 일치 여부 판단")
    void whenCallIsSameAddress_thenReturnBoolean() {
        // 1. 공백 기준으로 앞 두 글자 포함 여부 비교 (짧은 주소 기준)
        String address1 = "서울 중구 다동 155";
        String address2 = "서울특별시 중구 다동 155 2층 206~209호";
        assertEquals(ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"isSameAddress", address1, address2), Boolean.TRUE);

        // 2. 특수문자 또는 숫자가 포함된 경우 단어로 포함 여부 비교 (짧은 주소 기준)
        address1 = "서울 중구 다동[A] 155";
        address2 = "서울특별시 중구 다동 155 2층 206~209호";
        assertEquals(ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"isSameAddress", address1, address2), Boolean.FALSE);

        address1 = "서울 중구 다동 155";
        address2 = "서울특별시 중구 다동 156 2층 206~209호";
        assertEquals(ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"isSameAddress", address1, address2), Boolean.FALSE);
    }

    @Test
    @DisplayName("5) 주소를 규칙에 맞게 Set으로 변환")
    void whenCallAddressConvertToSet_thenReturnSet() {
        // 공백 기준으로 단어를 구분하여 다음 규칙에 맞게 Set으로 반환
        // - 특수문자: 포함시 단어 전체 반환
        // - 숫자: 포함시 단어 전체 반환
        // - 문자: 앞에서 부터 2글자만 반환
        String address = "서울특별시 중구 다동 155 2층 206~209호";
        Set<String> serviceWillReturn = Set.of("서울", "중구", "다동", "155", "2층", "206~209호");
        Set<String> serviceReturn = ReflectionTestUtils.invokeMethod(new SearchLocalService(kakaoClient, naverClient),"addressConvertToSet", address);
        assertTrue(serviceReturn.size()==serviceWillReturn.size());
        for (String word : serviceWillReturn) {
            assertTrue(serviceReturn.remove(word));
        }
    }
}
