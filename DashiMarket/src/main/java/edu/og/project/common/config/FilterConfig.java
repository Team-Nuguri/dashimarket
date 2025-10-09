package edu.og.project.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.og.project.common.filter.LoginFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<LoginFilter> loginFilter() {
		
		FilterRegistrationBean<LoginFilter> regiRegistrationBean = new FilterRegistrationBean<LoginFilter>();
		
		regiRegistrationBean.setFilter (new LoginFilter());
		
		String[] url = {"/myPage/*", "/board2/*"};
		regiRegistrationBean.setUrlPatterns(Arrays.asList(url)); // url 패턴 여러개 지정
		regiRegistrationBean.setName("loginFilter");
		regiRegistrationBean.setOrder(1);
		return regiRegistrationBean;
	}
}
