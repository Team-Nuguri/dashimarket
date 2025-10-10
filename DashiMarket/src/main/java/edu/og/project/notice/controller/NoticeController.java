package edu.og.project.notice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.og.project.notice.model.dto.Notice;
import edu.og.project.notice.model.service.NoticeService;

@Controller
public class NoticeController {
	
	@Autowired
	private NoticeService service;
	
	// 공지사항 목록 조회
	@GetMapping("/{boardType:n.*}")
	public String selectNoticeList(@PathVariable("boardType") String boardType, Model model) {
		List<Notice> noticeList = service.selectNoticeList(boardType);
		model.addAttribute("notice", noticeList);
		return "notice/notice";
	}
	
	// 공지사항 상세 조회
	
	
	
	
	
}
