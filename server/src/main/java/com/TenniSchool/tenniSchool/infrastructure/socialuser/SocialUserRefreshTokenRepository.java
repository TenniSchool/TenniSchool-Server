package com.TenniSchool.tenniSchool.infrastructure.socialuser;

import com.TenniSchool.tenniSchool.domain.SocialUser;
import com.TenniSchool.tenniSchool.token.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialUserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    UserRefreshToken findByUserId(String userId);
    UserRefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);

//    void saveAndFlush(UserRefreshToken userRefreshToken);
}
