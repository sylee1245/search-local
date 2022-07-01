package com.test.searchlocal.application.rest.external;

import com.test.searchlocal.domain.model.KeywordInfo;
import com.test.searchlocal.domain.model.LocalInfo;
import com.test.searchlocal.domain.service.KeywordService;
import com.test.searchlocal.domain.service.SearchLocalService;
import com.test.searchlocal.infrastructure.config.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
public class SearchLocalControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private SearchLocalService searchLocalService;
    @MockBean
    private KeywordService keywordService;

    @BeforeEach
    public void setup(WebApplicationContext ctx,
                      RestDocumentationContextProvider restDocumentation){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("1) 장소 검색, status: 200")
    void givenValidParameter_whenCallSearch_thenStatusIsOk() throws Exception{
        final String keyword = "곱창";
        List<LocalInfo> resultList = IntStream.range(1,4).mapToObj(i -> LocalInfo.builder()
                .name("곱창"+i)
                .address("곱창 주소"+i)
                .roadAddress("곱창 도로명 주소"+i)
                .build()).collect(Collectors.toList());

        given(searchLocalService.search(keyword)).willReturn(resultList);

        mockMvc.perform(get("/search")
                        .param("keyword", keyword)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("search-success",
                        responseFields(
                                fieldWithPath("totalCount").description("검색된 장소 목록 count"),
                                fieldWithPath("list[].name").description("장소명"),
                                fieldWithPath("list[].address").description("주소"),
                                fieldWithPath("list[].roadAddress").description("도로명 주소")
                        )
                ));
    }

    @Test
    @DisplayName("2-1) 유효하지 않은 파라미터로 조회시, status: 400")
    void givenValidParameter_whenCallSearch_thenStatusIs400() throws Exception{
        // 보내지 않은 경우
        String keyword = "";
        mockMvc.perform(get("/search")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("search-fail1"));

        // 빈 값인 경우
        keyword = "";
        mockMvc.perform(get("/search")
                .param("keyword", keyword)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("search-fail2"));

        // 최대 글자수(50)을 초과 경우
        keyword = "일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일";
        mockMvc.perform(get("/search")
                .param("keyword", keyword)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("search-fail3"));
    }

    @Test
    @DisplayName("검색 키워드 목록")
    void whenCallSearchTopList_thenStatusIsOk() throws Exception{
        givenValidParameter_whenCallSearch_thenStatusIsOk();
        List<KeywordInfo> resultList = IntStream.range(1,4)
                .mapToObj(i -> new KeywordInfo("곱창"+i, 5-i))
                .collect(Collectors.toList());

        given(keywordService.searchTopList(Constants.AGGREGATE_TOP_COUNT)).willReturn(resultList);

        mockMvc.perform(get("/search/top")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("search-top-success",
                responseFields(
                    fieldWithPath("totalCount").description("검색된 키워드 Top 목록 count"),
                    fieldWithPath("list[].keyword").description("키워드"),
                    fieldWithPath("list[].count").description("검색된 횟수")
                )
            ));
    }

}
