package com.test.searchlocal.application.rest.external;

import com.test.searchlocal.application.rest.response.SearchListResponse;
import com.test.searchlocal.domain.service.KeywordService;
import com.test.searchlocal.domain.service.SearchLocalService;
import com.test.searchlocal.infrastructure.config.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequiredArgsConstructor
@RequestMapping("search")
@Validated
public class SearchLocalController {
    private final SearchLocalService searchLocalService;
    private final KeywordService keywordService;

    /**
     * 장소 검색
     */
    @GetMapping
    public SearchListResponse search(@NotNull
                                     @NotBlank(message = "'keyword' is not blank")
                                     @Size(max = 50, message = "'keyword' max length is 50")
                                     @RequestParam("keyword") String keyword) {
        keywordService.save(keyword);
        return new SearchListResponse<>(searchLocalService.search(keyword));
    }

    /**
     * 검색 키워드 목록
     */
    @GetMapping("/top")
    public SearchListResponse searchTopList() {
        return new SearchListResponse<>(keywordService.searchTopList(Constants.AGGREGATE_TOP_COUNT));
    }
}
