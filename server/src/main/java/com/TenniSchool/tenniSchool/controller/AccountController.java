package com.TenniSchool.tenniSchool.controller;

import com.TenniSchool.tenniSchool.account.GetSocialOAuthRes;
import com.TenniSchool.tenniSchool.common.dto.ApiResponse;
import com.TenniSchool.tenniSchool.config.Constant;
import com.TenniSchool.tenniSchool.controller.dto.response.UserLoginResponseDto;
import com.TenniSchool.tenniSchool.exception.Error;
import com.TenniSchool.tenniSchool.exception.Success;
import com.TenniSchool.tenniSchool.exception.model.TennisException;
import com.TenniSchool.tenniSchool.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.TenniSchool.tenniSchool.config.Constant.SocialLoginType.GOOGLE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/accounts")
public class AccountController {
    /*
    유저 소셜 로그인으로 리다이렉트 해주는 url
    [get] /accounts/auth
    @return void
    흠 @NoAuth식으로 해놧는데 저게 뭐지
     */
    private final OAuthService oAuthService;

    @GetMapping("/auth/google")
    public void googleRedirect() {
        try {
            oAuthService.request(GOOGLE);
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
    }

    /*@GetMapping("/auth/{socialLoginType}")//GOOGLE이 들어올 것이다.
    public void SocialLoginRedirect(@PathVariable(name = "socialLoginType") String SocialLoginPath) throws IOException {
        Constant.SocialLoginType socialLoginType = Constant.SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.request(socialLoginType);
    }*/
    /*@ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/callback")
    public ApiResponse<GetSocialOAuthRes> callback(         //String으로 변경해놨음.ApiResponse<GetSocialOAuthRes> 원래 이거임
            @PathVariable(name = "socialLoginType")String socialLoginPath,
            @RequestParam(name = "code")String code)throws IOException,TennisException { //원래 tennisException도 있음.
        System.out.println(">> 소셜 로그인 API서버로부터 받은 code :"+ code);
        Constant.SocialLoginType socialLoginType= Constant.SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLogin(socialLoginType,code);
        return ApiResponse.success(Success.LOGIN_SUCCESS,getSocialOAuthRes); //원래 Apiresponse.Success 이런식으로 들어갔음.
    }*/

    @ResponseBody
    @GetMapping(value = "/auth/google/callback")
    public ApiResponse<GetSocialOAuthRes> callback(
            @RequestParam(name = "code") String code
    ) throws IOException { //원래 tennisException도 있음.

        System.out.println(">> 소셜 로그인 API서버로부터 받은 code :" + code);
        GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLogin(GOOGLE,code);

        return ApiResponse.success(Success.LOGIN_SUCCESS, getSocialOAuthRes);
    }


}
