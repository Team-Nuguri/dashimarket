package edu.og.project.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import edu.og.project.oauth2.handler.CustomOAuth2SuccessHandler;
import edu.og.project.oauth2.service.OAuth2UserService;

@Configuration // 설정용 Bean 생성 클래스
@EnableWebSecurity
public class SecurityConfig {

	@Bean // @Bean이 작성된 메소드에서 반환된 객체는 Spring Container가 관리함
	public BCryptPasswordEncoder bCryptPasswordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Autowired 
	private OAuth2UserService oAuth2UserService;
	
	@Autowired
	private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
	 

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 1. HTTP 요청에 대한 권한 설정
				.authorizeHttpRequests(authorize -> authorize

						// 1-1. 정적 리소스 경로는 항상 허용 (MIME 타입 에러 방지 포함)
						.requestMatchers(AntPathRequestMatcher.antMatcher("/css/**"),
								AntPathRequestMatcher.antMatcher("/js/**"),
								AntPathRequestMatcher.antMatcher("/images/**"),
								AntPathRequestMatcher.antMatcher("/.well-known/**"))
								
						.permitAll()

						// 1-2. [핵심 수정] 명시적으로 인증이 필요한 경로를 먼저 설정
						// 예시: 마이페이지, 프로필 수정 등은 로그인 필요
						.requestMatchers("/member/myPage").authenticated()

						.anyRequest().permitAll())

				// OAuth2 소셜 로그인 기능 활성화
				.oauth2Login(oauth2 -> oauth2
						// 커스텀 로그인 페이지
						.loginPage("/member/login")
						// 로그인 성공이 완료되면 홈으로 이동
						.defaultSuccessUrl("/") 
						// 인가코드 요청 / 수신
						// 엑세스 토큰 획득
						// 카카오 로그인 후 넘어온 사용자 정보 처리
						.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
						// 로그인 성공 시 실행되는 로직 -> 회원 정보 세션에 올려서 로그인 처리
						.successHandler(customOAuth2SuccessHandler)
						)
						

				// 3. 로그아웃 설정
				
				.csrf(c -> c.disable());

		return http.build();
	}

}
