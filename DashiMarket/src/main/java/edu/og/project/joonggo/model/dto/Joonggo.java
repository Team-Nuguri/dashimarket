package edu.og.project.joonggo.model.dto;

import java.util.List;

import edu.og.project.common.dto.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Joonggo {
	
	private String joonggoNo;
	private String joonggoTitle;
	private String joonggoContent;
	private String joonggoCreateDate;
	private String joonggoUpdateDate;
	private int joonggoPrice;
	private int readCount;
	private int boardCode;
	private String boardType;
	private String thumbnail;
	
	// 이미지
	private Image image;
		
	
	
	// 좋아요 수 서븨쿼리로
	private int likeCount;
	
	// 카테고리
	private String categoryId;
	private String parentCategoryNo;
	private String mainCategory;
	private String subCategory;
	
	// 회원
	private String memberNickname;
	private int memberNo;
	private String profileImage;
	private String defaultDong;
	
	
	List<Image> imageList;
	
	
	// 헤더 검색
	private String boardNo;
	private String boardTitle;
	
	

}
