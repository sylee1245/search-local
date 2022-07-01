package com.test.searchlocal.infrastructure.client.naverApi.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class NaverLocalInfo {
    private String address;
    private String roadAddress;
    private String title;
    private String category;
    private String link;
}
