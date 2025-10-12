package edu.og.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
// 보안 관련 자동 설정 사용 x

@SpringBootApplication(exclude =SecurityAutoConfiguration.class)
@EnableTransactionManagement
public class DashiMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashiMarketApplication.class, args);
	}

}
