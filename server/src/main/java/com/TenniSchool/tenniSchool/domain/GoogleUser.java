package com.TenniSchool.tenniSchool.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//구글(서드파티)로 엑세스 토클을 보내서 받아올 구글에 등록된 사용자 정보
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoogleUser {
    public String id;
    public String email;
    public Boolean verifiedEmail;
    public String name;
    public String givenName;
    public String familyName;
    public String picture;
    public String locale;
}
