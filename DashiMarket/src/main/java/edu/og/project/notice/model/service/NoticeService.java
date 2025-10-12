package edu.og.project.notice.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.notice.model.dto.Notice;

public interface NoticeService {
	
	// 공지사항 목록 조회 (검색 + 페이징)
    List<Notice> selectNoticeList(Map<String, Object> paramMap);
    
    // 공지사항 전체 개수 조회 (검색 포함)
    int getNoticeCount(String query);
    
    // 공지사항 상세 조회
    Notice selectNoticeDetail(int noticeNo);
    
    // 공지사항 글 등록
    int insertNotice(Notice notice);
    
    // 이전글 조회
    Notice selectPrevNotice(int noticeNo);
    
    // 다음글 조회
    Notice selectNextNotice(int noticeNo);
    
    // 공지사항 수정
    int updateNotice(Notice notice);
    
    // 공지사항 삭제
    int deleteNotice(int noticeNo);
    
    // 조회수 증가
    int increaseViewCount(int noticeNo);
	
	
	
	

}
