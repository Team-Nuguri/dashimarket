package edu.og.project.afterTradeReview.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewWrite {
	
	private String reviewNo;
	private String comment; 
	private MultipartFile image;
	private int goodsPrice;
	private int goodsStock;
	
	private int memberNo;
	private String boardType;

}
