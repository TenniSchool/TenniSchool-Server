package com.TenniSchool.tenniSchool.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//구글에 일회성 코드를 다시보내서 받아올 엑세스 토큰을 포함한 json문자열을 담을 클래스
@AllArgsConstructor
@Getter
@Setter
public class GoogleOAuthToken {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
