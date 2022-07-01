package com.test.searchlocal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 장소 도메인 정보
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalInfo {
    private String name;
    private String address;
    private String roadAddress;
}
