package com.TenniSchool.tenniSchool.controller;

import com.TenniSchool.tenniSchool.common.dto.ApiResponse;
import com.TenniSchool.tenniSchool.domain.SocialUser;
import com.TenniSchool.tenniSchool.exception.Success;
import com.TenniSchool.tenniSchool.service.socialuser.SocialUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SocialUserController {

    private final SocialUserService userService;

    @GetMapping
    public ApiResponse getUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SocialUser user = userService.getUser(principal.getUsername());

        return ApiResponse.success(Success.LOGIN_SUCCESS, user);
    }
}

