package edu.og.project.oauth2.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User; // 인터페이스 직접 구현으로 변경

import edu.og.project.common.dto.Member;
import lombok.Getter;


@Getter 
public class CustomOAuth2User implements OAuth2User, Serializable {


    // 세션 저장을 위한 직렬화 ID
    private static final long serialVersionUID = 1L; 

    // 로그인된 실제 회원 정보 DTO
    private final Member member; 
    
    // 소셜 로그인 제공자로부터 받은 원본 속성 (ID, 닉네임, 이메일 등)
    private final Map<String, Object> attributes;
    
    // 사용자 ID를 식별하는 키 
    private final String nameAttributeKey; 

    // 권한 목록 (임시로 ROLE_USER 권한 부여)
    private final Collection<? extends GrantedAuthority> authorities = 
        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    


    public CustomOAuth2User(Member member, Map<String, Object> attributes, String nameAttributeKey) {
        this.member = member;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        System.out.println("CustomOAuth2User 확인 : " + member);
    }

    
    @Override
    public String getName() {
        Object principalId = attributes.get(nameAttributeKey);
        return principalId != null ? String.valueOf(principalId) : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
