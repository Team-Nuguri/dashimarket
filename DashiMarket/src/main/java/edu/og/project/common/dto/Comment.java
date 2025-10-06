package edu.og.project.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Comment {
	
	private int commentNo; // 댓글 번호
	private String postNo; // 게시글 번호
	private int parentCommentNo; // 부모 댓글 번호
	
	private String commentContent; // 댓글 내용
	private String commentCreateDate; // 작성일
	private String commentUpdateDate; // 수정일
	private String commentStatus; // 댓글 상태(삭제, 비밀, 미답변, 답변완료)
	
	private int memberNo; // 회원 번호
	private String memberNickname; // 닉네임
	private String profileImage; // 프로필 사진
	
	private int level; // 계층형 쿼리 사용

}
