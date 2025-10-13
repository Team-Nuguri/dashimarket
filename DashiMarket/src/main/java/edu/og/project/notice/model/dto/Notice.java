package edu.og.project.notice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

	// 기본 게시판 틀
	private String noticeNo;
	private String noticeTitle;
	private String noticeContent;
	private String noticeCreateDate;
	private int readCount;
	private int boardCode; // 게시판 코드 (공지사항 : 4)
	private String boardType; // 게시판 이름 (notice)

	// 회원
	private String memberNickname;
	private int memberNo;

	// 첨부파일 추가
	private String attachmentPath; // 이미지 파일 웹 경로 (/images/notice/파일명.jpg)
	private String attachmentOriginalName; // 원본 파일명
	
}
