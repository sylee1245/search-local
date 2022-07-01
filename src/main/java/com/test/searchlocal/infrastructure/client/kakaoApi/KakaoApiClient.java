package com.test.searchlocal.infrastructure.client.kakaoApi;

import com.test.searchlocal.infrastructure.client.kakaoApi.response.KakaoLocalInfoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KakaoApiClient {

    @GET("v2/local/search/keyword.{format}")
    Call<KakaoLocalInfoResponse> searchKeyword(
            @Path("format") String format,
            @Header("Authorization") String token,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sort") String sort,
            @Query("query") String query
    );

}
