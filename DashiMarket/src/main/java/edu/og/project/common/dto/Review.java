package edu.og.project.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Review {
	
	private String reviewNo;
	private String comment;
	private String reviewCreateDate;
	private String reviewImgPath; // 후기 사진 경로
	private String reviewImgRename; // 후기 사진 이름
	private int buyerNo; // 구매자 번호
	private String boardNo; // 게시글 번호
	private double rating;
	
	
}
