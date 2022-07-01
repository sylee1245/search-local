package com.test.searchlocal.infrastructure.client.kakaoApi.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class KakaoLocalInfo {
    private String addressName;
    private String roadAddressName;
    private String placeName;
    private String categoryName;
    private String placeUrl;
}
