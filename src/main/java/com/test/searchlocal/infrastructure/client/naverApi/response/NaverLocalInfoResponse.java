package com.test.searchlocal.infrastructure.client.naverApi.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class NaverLocalInfoResponse {
    private List<NaverLocalInfo> items;
}
