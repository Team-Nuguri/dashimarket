package edu.og.project.notice.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notice {
	
	// 기본 게시판 틀
	private String noticeNo;
	private String noticeTitle;
	private String noticeContent;
	private String noticeCreateDate;
	private int readCount;
	private int boardCode; // 게시판 코드 (공지사항 : 4)
	private String boardType; // 게시판 이름 (notice)
	
}
