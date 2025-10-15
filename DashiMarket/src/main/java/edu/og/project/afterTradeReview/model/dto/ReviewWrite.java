package edu.og.project.afterTradeReview.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewWrite {
	
	/*
	 * private int rating1; 
	 * private int rating2; 
	 * private int rating3; 
	 * private int rating4;
	 */
	
	 private int question1; 
	 private int question2; 
	 private int question3; 
	 private int question4;
	 
	 private long reviewNo;
	 private String comment;
	 private String reviewType;
	 private int buyerNo;
	 private String boardNo; 
	 
	 // DB 저장용
	 private String reviewFilePath;   // 업로드 경로
	 private String reviewFileName;   // 실제 저장된 파일명

	 // 업로드용 (폼에서 넘어오는 파일)
	 private MultipartFile reviewImage;
	    
}
