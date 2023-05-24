package com.TenniSchool.tenniSchool.infrastructure.socialuser;

import com.TenniSchool.tenniSchool.domain.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {
    SocialUser findByUserId(String userId);
}
