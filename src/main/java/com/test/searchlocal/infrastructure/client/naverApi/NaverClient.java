package com.test.searchlocal.infrastructure.client.naverApi;

import com.test.searchlocal.application.rest.exception.CustomException;
import com.test.searchlocal.application.rest.exception.code.EnumErrorType;
import com.test.searchlocal.domain.model.LocalInfo;
import com.test.searchlocal.infrastructure.client.SearchClient;
import com.test.searchlocal.infrastructure.client.naverApi.code.NaverSortType;
import com.test.searchlocal.infrastructure.client.naverApi.response.NaverLocalInfoResponse;
import com.test.searchlocal.infrastructure.config.ApplicationProperties;
import com.test.searchlocal.infrastructure.config.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverClient implements SearchClient {
    private final ApplicationProperties applicationProperties;
    private final NaverApiClient naverApiClient;
    private final ModelMapper modelMapper;

    public List<LocalInfo> searchKeyword(String keyword, int page, int size) {
        Call<NaverLocalInfoResponse> call = naverApiClient.searchKeyword(Constants.API_FORMAT,
                applicationProperties.getClient().getNaver().getClientId(),
                applicationProperties.getClient().getNaver().getClientSecret(),
                page,
                size,
                NaverSortType.COMMENT.getCode(),
                keyword
        );
        try {
            Response<NaverLocalInfoResponse> response = call.execute();
            if (response.isSuccessful()) {
                NaverLocalInfoResponse naverResponse = response.body();
                if (naverResponse == null) {
                    return Collections.emptyList();
                }
                return naverResponse.getItems().stream().map(d -> modelMapper.map(d, LocalInfo.class)).collect(Collectors.toList());
            }
            log.error("Naver client api response fail");
            throw new CustomException(EnumErrorType.CLIENT_API_SERVER_ERROR);
        } catch (IOException e) {
            log.error("Error has occurred while requesting Naver client api", e.getMessage());
            throw new CustomException(EnumErrorType.CLIENT_API_SERVER_ERROR);
        }
    }
}
