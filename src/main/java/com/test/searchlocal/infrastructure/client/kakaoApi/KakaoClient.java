package com.test.searchlocal.infrastructure.client.kakaoApi;

import com.test.searchlocal.application.rest.exception.CustomException;
import com.test.searchlocal.application.rest.exception.code.EnumErrorType;
import com.test.searchlocal.domain.model.LocalInfo;
import com.test.searchlocal.infrastructure.client.SearchClient;
import com.test.searchlocal.infrastructure.client.kakaoApi.code.KakaoSortType;
import com.test.searchlocal.infrastructure.client.kakaoApi.response.KakaoLocalInfoResponse;
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
public class KakaoClient implements SearchClient {
    private final ApplicationProperties applicationProperties;
    private final KakaoApiClient kakaoApiClient;
    private final ModelMapper modelMapper;

    @Override
    public List<LocalInfo> searchKeyword(String keyword, int page, int size) {
        Call<KakaoLocalInfoResponse> call = kakaoApiClient.searchKeyword(Constants.API_FORMAT,
                applicationProperties.getClient().getKakao().getToken(),
                page,
                size,
                KakaoSortType.ACCURACY.getCode(),
                keyword
        );
        try {
            Response<KakaoLocalInfoResponse> response = call.execute();
            if (response.isSuccessful()) {
                KakaoLocalInfoResponse kakaoResponse = response.body();
                if (kakaoResponse == null) {
                    return Collections.emptyList();
                }
                return kakaoResponse.getDocuments().stream().map(d -> modelMapper.map(d, LocalInfo.class)).collect(Collectors.toList());
            }
            log.error("Kakao client api response fail");
            throw new CustomException(EnumErrorType.CLIENT_API_SERVER_ERROR);
        } catch (IOException e) {
            log.error("Error has occurred while requesting Kakao client api", e.getMessage());
            throw new CustomException(EnumErrorType.CLIENT_API_SERVER_ERROR);
        }
    }

}
