package edu.og.project.notice.model.service;

import java.util.List;

import edu.og.project.notice.model.dto.Notice;

public interface NoticeService {
	
	// 공지사항 목록 조회
	List<Notice> selectNoticeList(String boardType);
	
	// 공지사항 글 등록
	int insertNotice(Notice notice);
	
	

}
