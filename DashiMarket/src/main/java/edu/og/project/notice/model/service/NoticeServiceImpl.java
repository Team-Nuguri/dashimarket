package edu.og.project.notice.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.notice.model.dao.NoticeMapper;
import edu.og.project.notice.model.dto.Notice;

@Service
public class NoticeServiceImpl implements NoticeService{
	
	@Autowired
	private NoticeMapper mapper;
	
	@Override
	public List<Notice> selectNoticeList(String boardType) {
		return mapper.selectNoticeList(boardType);
	}
	

}
