package edu.og.project.community.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Commnity {

	private String boardNo; // 굿즈 상품 번호
	
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
	
	// 회원 테이블과 join
	private int memberNo; // 회원 번호
	private String memberNickname;
	private String profileImage;
	private String thumbnail;
	
	// 이미지
//	private List<Image> imgList;
	
	// 댓글 목록
//	private List<Comment> commentList;
	
	// 댓글 수
	private int commentCount;
	
	// 좋아요 수
	private int likeCount;
}
