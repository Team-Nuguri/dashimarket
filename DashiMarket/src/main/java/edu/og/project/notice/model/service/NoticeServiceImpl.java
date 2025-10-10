package edu.og.project.notice.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.project.notice.model.dao.NoticeMapper;
import edu.og.project.notice.model.dto.Notice;

@Service
public class NoticeServiceImpl implements NoticeService{
	
	@Autowired
	private NoticeMapper mapper;
	
	// 공지사항 목록 조회
	public List<Notice> selectNoticeList(String boardType) {
		return mapper.selectNoticeList(boardType);
	}
	
	
	// 공지사항 글 등록
	@Transactional 
	public int insertNotice(Notice notice) {
		return mapper.insertNotice(notice);
	}
	

}
