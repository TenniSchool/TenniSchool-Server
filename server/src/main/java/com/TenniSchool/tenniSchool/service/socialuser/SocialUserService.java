package com.TenniSchool.tenniSchool.service.socialuser;

import com.TenniSchool.tenniSchool.domain.SocialUser;
import com.TenniSchool.tenniSchool.infrastructure.socialuser.SocialUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialUserService {
    private final SocialUserRepository userRepository;

    public SocialUser getUser(String userId) {
        return userRepository.findByUserId(userId);
    }
}

