package com.TenniSchool.tenniSchool.info;

import com.TenniSchool.tenniSchool.info.impl.GoogleOAuth2UserInfo;
import com.TenniSchool.tenniSchool.oauth.entity.ProviderType;

import java.util.Map;

import static com.TenniSchool.tenniSchool.oauth.entity.ProviderType.GOOGLE;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
//            case FACEBOOK: return new FacebookOAuth2UserInfo(attributes);
//            case NAVER: return new NaverOAuth2UserInfo(attributes);
//            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}

