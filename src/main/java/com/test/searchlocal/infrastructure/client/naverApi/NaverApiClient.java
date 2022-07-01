package com.test.searchlocal.infrastructure.client.naverApi;

import com.test.searchlocal.infrastructure.client.naverApi.response.NaverLocalInfoResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface NaverApiClient {

    @GET("/v1/search/local.{format}")
    @Headers("Content-Type: plain/text")
    Call<NaverLocalInfoResponse> searchKeyword(
            @Path("format") String format,
            @Header("X-Naver-Client-Id") String clientId,
            @Header("X-Naver-Client-Secret") String clientSecret,
            @Query("start") int start,
            @Query("display") int display,
            @Query("sort") String sort,
            @Query("query") String query
    );

}
