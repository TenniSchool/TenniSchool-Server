package com.TenniSchool.tenniSchool.controller;

import com.TenniSchool.tenniSchool.common.dto.ApiResponse;
import com.TenniSchool.tenniSchool.config.jwt.JwtService;
import com.TenniSchool.tenniSchool.controller.dto.request.UserLoginRequestDto;
import com.TenniSchool.tenniSchool.controller.dto.request.UserRequestDto;
import com.TenniSchool.tenniSchool.controller.dto.response.UserLoginResponseDto;
import com.TenniSchool.tenniSchool.controller.dto.response.UserResponseDto;
import com.TenniSchool.tenniSchool.exception.Success;
import com.TenniSchool.tenniSchool.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponseDto> create(@RequestBody @Valid final UserRequestDto request){
        return ApiResponse.success(Success.SIGNUP_SUCCESS, userService.create(request));
    }
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UserLoginResponseDto> login(@RequestBody @Valid final UserLoginRequestDto request){
        final Long userId = userService.login(request);
        final String token = jwtService.issuedToken(String.valueOf(userId));
        return ApiResponse.success(Success.LOGIN_SUCCESS, UserLoginResponseDto.of(userId, token));
    }
}
