package edu.og.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${my.profile.location}")
    private String profileLocation;
    
    @Value("${my.profile.webpath}")
    private String profileWebpath;
    
    @Value("${my.notice.location}")
    private String noticeLocation;
    
    @Value("${my.notice.webpath}")
    private String noticeWebpath;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 프로필 이미지 경로 매핑
        registry.addResourceHandler(profileWebpath + "**")
                .addResourceLocations("file:///" + profileLocation);
        
	    // 공지사항/FAQ 이미지 경로 매핑
	    registry.addResourceHandler(noticeWebpath + "**")
	            .addResourceLocations("file:///" + noticeLocation);
	    System.out.println("공지사항/FAQ: " + noticeWebpath + " -> " + noticeLocation);
    }
}