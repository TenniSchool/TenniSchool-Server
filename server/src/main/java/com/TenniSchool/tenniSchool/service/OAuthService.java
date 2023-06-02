package com.TenniSchool.tenniSchool.service;
import com.TenniSchool.tenniSchool.account.GetSocialOAuthRes;
import com.TenniSchool.tenniSchool.account.GoogleOAuthToken;
import com.TenniSchool.tenniSchool.config.Constant;
import com.TenniSchool.tenniSchool.account.GoogleOauth;
import com.TenniSchool.tenniSchool.config.jwt.JwtService;
import com.TenniSchool.tenniSchool.domain.GoogleUser;
import com.TenniSchool.tenniSchool.domain.User;
import com.TenniSchool.tenniSchool.infrastructure.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


//로그인 방식에 따라서 해당 클래스를 호출해줄 Service class
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    private final JwtService jwtService;

    public void request(Constant.SocialLoginType socialLoginType) throws IOException{
        String redirectURL;
        switch (socialLoginType){
            case GOOGLE: {
                //각 소셜 로그인을 요청하면 소셜 로그인 페이지로 리다이렉트 해주는 프로세스이다.
                redirectURL = googleOauth.getOauthRedirectURL();
            }break;
            default:{
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }
        System.out.println("redirecturl입니다 : "+redirectURL);
        response.sendRedirect(redirectURL);

    }

    public GetSocialOAuthRes oAuthLogin(Constant.SocialLoginType socialLoginType, String code) throws IOException{
        switch (socialLoginType) {
            case GOOGLE: {
                //구글로 일회성 코드를 보내서 액세스 토큰이 담긴 응답객체를 받아온다.
                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
                System.out.println(accessTokenResponse);
                //응답 객체가 json형식으로 되어있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);
                System.out.println(oAuthToken);
                System.out.println("이게 oAuthtoken");
                //엑세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
                System.out.println(userInfoResponse);
                               //다시 json형식의 응답객체를 자바 객체로 역직렬화 한다.
                GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);
                System.out.println("현재 구글 유저 : " + googleUser);

                String user_id = googleUser.getEmail();
                String jwtToken = jwtService.issuedToken(user_id);
                GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(jwtToken, user_id, oAuthToken.getAccess_token(),oAuthToken.getToken_type());
                return getSocialOAuthRes;


                //우리 서버의 db와 대조하여 해당 user가 존재하는 지 확인한다.
//                int user_num = accountProvider.getUserNum(user_id);
//                UserRepository userRepository = null;
//                Optional<User> present_user = userRepository.findByEmail(user_id); //원래는 존재여부 확인하는 듯
//                Long user_num = present_user.get().getId();
//                if(userRepository.existsByEmail(user_id)){
//                    //서버에 user가 존재하면 앞으로 회원 인가 처리를 위한 jwtToken을 발급한다.
//                    String jwtToken = jwtService.issuedToken(user_id); //usernum 걍 없애버림
//                    //액세스 토큰과 jwtToken, 이외 정보들이 담긴 자바 객체를 다시 전송한다.
//                    GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(jwtToken, Math.toIntExact(user_num), oAuthToken.getAccess_token(),oAuthToken.getToken_type());
//                    System.out.println("해치웠나?");
//                    return getSocialOAuthRes;
//                }
//                else {
//                    String jwtToken = jwtService.issuedToken(user_id); //usernum 걍 없애버림
//                    //액세스 토큰과 jwtToken, 이외 정보들이 담긴 자바 객체를 다시 전송한다.
//                    GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(jwtToken, Math.toIntExact(user_num), oAuthToken.getAccess_token(),oAuthToken.getToken_type());
//                    System.out.println("해치웠나?");
//                    return getSocialOAuthRes;
//                }
            }

            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }
    }

}
