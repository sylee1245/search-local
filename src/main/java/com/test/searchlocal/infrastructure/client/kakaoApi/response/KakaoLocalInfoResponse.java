package com.test.searchlocal.infrastructure.client.kakaoApi.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class KakaoLocalInfoResponse {
    private List<KakaoLocalInfo> documents;
}
