package com.test.searchlocal.infrastructure.config;

import com.test.searchlocal.domain.model.LocalInfo;
import com.test.searchlocal.infrastructure.client.kakaoApi.response.KakaoLocalInfo;
import com.test.searchlocal.infrastructure.client.naverApi.response.NaverLocalInfo;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mapper 설정
 */
@Configuration
public class Config {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setDeepCopyEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(KakaoLocalInfo.class, LocalInfo.class)
                .addMappings(mapper -> {
                    mapper.map(KakaoLocalInfo::getPlaceName, LocalInfo::setName);
                    mapper.map(KakaoLocalInfo::getAddressName, LocalInfo::setAddress);
                    mapper.map(KakaoLocalInfo::getRoadAddressName, LocalInfo::setRoadAddress);
                });
        modelMapper.typeMap(NaverLocalInfo.class, LocalInfo.class)
                .addMappings(mapper -> {
                    mapper.map(NaverLocalInfo::getTitle, LocalInfo::setName);
                });
        return modelMapper;
    }
}
