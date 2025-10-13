package edu.og.project.community.model.dto;

import java.util.List;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Community {
	
	// 기본 게시판 틀
	private String communityNo;
	private String communityTitle;
	private String communityContent;
	private String communityCreateDate;
	private String communityUpdateDate;
	private int postViews;
	private int boardCode; // 게시판 코드 (커뮤니티: 3)
	private String boardType; // 게시판 이름(community)
	private String thumbnail;

	// 카테고리
	private String categoryId;
	private String categoryName;
	
	// 회원
	private String memberNickname;
	private int memberNo;
	private String profilePath;
	private String defaultDong;
	
	// 이미지
	private List<Image> imgList;
	
	// 댓글
	private List<Comment> commentList;
	
	// 전체 댓글 수
	private int commentCount;
	
	// 좋아요 수
	private int likeCount;
	
	// 좋아요 여부
	private int likeCheck;
}
