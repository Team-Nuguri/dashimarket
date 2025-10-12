package edu.og.project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // 설정용 Bean 생성 클래스
public class SecurityConfig {

	@Bean // @Bean이 작성된 메소드에서 반환된 객체는 Spring Container가 관리함
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
}
