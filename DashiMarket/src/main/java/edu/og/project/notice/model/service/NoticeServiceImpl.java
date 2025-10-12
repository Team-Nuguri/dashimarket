package edu.og.project.notice.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.project.notice.model.dao.NoticeMapper;
import edu.og.project.notice.model.dto.Notice;

@Service
public class NoticeServiceImpl implements NoticeService{
	
	@Autowired
	private NoticeMapper mapper;
	
	// 공지사항 목록 조회 (검색 + 페이징)
    @Override
    public List<Notice> selectNoticeList(Map<String, Object> paramMap) {
        return mapper.selectNoticeList(paramMap);
    }
    
    // 공지사항 전체 개수 조회 (검색 포함)
    @Override
    public int getNoticeCount(String query) {
        return mapper.getNoticeCount(query);
    }
    
    // 공지사항 상세 조회
    @Override
    public Notice selectNoticeDetail(int noticeNo) {
        return mapper.selectNoticeDetail(noticeNo);
    }
    
    // 공지사항 글 등록
    @Transactional 
    @Override
    public int insertNotice(Notice notice) {
        return mapper.insertNotice(notice);
    }
    
    // 이전글 조회
    @Override
    public Notice selectPrevNotice(int noticeNo) {
        return mapper.selectPrevNotice(noticeNo);
    }
    
    // 다음글 조회
    @Override
    public Notice selectNextNotice(int noticeNo) {
        return mapper.selectNextNotice(noticeNo);
    }
    
    // 공지사항 수정
    @Transactional
    @Override
    public int updateNotice(Notice notice) {
        return mapper.updateNotice(notice);
    }
    
    // 공지사항 삭제 (논리 삭제)
    @Transactional
    @Override
    public int deleteNotice(int noticeNo) {
        return mapper.deleteNotice(noticeNo);
    }
    
    // 조회수 증가
    @Transactional
    @Override
    public int increaseViewCount(int noticeNo) {
        return mapper.increaseViewCount(noticeNo);
    }

	
	

}
