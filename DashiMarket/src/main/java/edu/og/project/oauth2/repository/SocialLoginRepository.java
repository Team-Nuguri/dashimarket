package edu.og.project.oauth2.repository;

import edu.og.project.oauth2.entity.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// SocialLogin 엔티티의 DB 작업을 위한 Repository
public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {

    /**
     * 소셜 서비스 제공자와 소셜 ID를 이용하여 연동 정보를 조회
     * @param provider 소셜 서비스 제공자 (예: KAKAO, GOOGLE)
     * @param socialId 소셜 서비스에서 발급된 고유 ID
     * @return Optional<SocialLogin>
     */
    Optional<SocialLogin> findByProviderAndSocialId(String provider, String socialId);
}
