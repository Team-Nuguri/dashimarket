package edu.og.project.goods.model.dto;

import java.util.List;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Image;
import edu.og.project.common.dto.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Goods {

	private String boardNo; // 굿즈 상품 번호(게시글 번호)
	
	// 굿즈 테이블과 join
	private int goodsPrice; // 굿즈 가격
	private int goodsStock; // 재고 수량
	
	// 기본 게시판 틀
	private String boardTitle; // 굿즈 제품명(게시글 제목)
	private String boardContent; // 굿즈 설명
	private String boardCreateDate; // 작성일
	private String boardUpdateDate; // 수정일
	private int postViews; // 조회수
	private int boardCode; // 게시판 코드
	private String thumbnail;
	
	// 이미지
	private Image image;
	
	// qna 목록 리스트
	private List<Comment> commentList;
	
	// 굿즈 리뷰 리스트
	private List<Review> reviewList;
	
	// 리뷰 수
	private int reviewCount;
	
	// 평점
	private double avgRating;
	
	// 장바구니 
	private int quantity;
	
	
}
