package com.test.searchlocal.infrastructure.client.kakaoApi.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KakaoSortType {
    ACCURACY("accuracy"),
    DISTANCE("distance");

    private final String code;
}