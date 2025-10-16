package edu.og.project.oauth2.repository;

import edu.og.project.oauth2.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Member 엔티티의 DB 작업을 위한 Repository
public interface MemberRepository extends JpaRepository<User, Long> {

    /**
     * 이메일을 이용하여 회원을 조회합니다.
     * OAuth2 로그인에서 신규/기존 회원 여부를 판단할 때 사용됩니다.
     * @param memberEmail 조회할 회원 이메일
     * @return Optional<Member>
     */
    Optional<User> findByMemberEmail(String memberEmail);
    
}
