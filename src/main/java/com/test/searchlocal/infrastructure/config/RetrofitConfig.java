package com.test.searchlocal.infrastructure.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.searchlocal.infrastructure.client.kakaoApi.KakaoApiClient;
import com.test.searchlocal.infrastructure.client.naverApi.NaverApiClient;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class RetrofitConfig {

    private final ApplicationProperties applicationProperties;

    /**
     * 레트로핏 공통 설정
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Kakao 레트로핏 설정
     */
    @Bean
    public KakaoApiClient kakaoApiClient(OkHttpClient client) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return new Retrofit.Builder().baseUrl(applicationProperties.getClient().getKakao().getUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(KakaoApiClient.class);
    }

    /**
     * naver 레트로핏 설정
     */
    @Bean
    public NaverApiClient naverApiClient(OkHttpClient client) {
        return new Retrofit.Builder().baseUrl(applicationProperties.getClient().getNaver().getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(NaverApiClient.class);
    }
}
