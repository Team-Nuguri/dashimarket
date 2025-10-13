package edu.og.project.notice.model.dto;

import java.util.List;

import edu.og.project.common.dto.Image;
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

	// 이미지 리스트 (IMAGE 테이블 사용)
	private List<Image> imageList;
	
}
