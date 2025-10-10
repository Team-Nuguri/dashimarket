package edu.og.project.notice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	
	// 관리자 전용 글쓰기 폼 페이지 이동
	@GetMapping("/notice/write")
	public String noticeWriteForm() {
		// 관리자 권한 체크는 여기서, 혹은 인터셉터에서 처리
		return "notice/notice-adminWrite";
	}
	
	
	// 관리자 전용 글쓰기  등록 처리
	@PostMapping("/notice/write")
	public String noticeWriteSubmit(
			@RequestParam("admWriteBoardSelect") String boardType,
			@RequestParam("admWriteTitle") String title,
			@RequestParam("admWriteContent") String content,
			@RequestParam(value = "admWriteFileInput", required = false)
			MultipartFile file,
			RedirectAttributes ra) {
		
		// Notice DTO 객체 생성 및 데이터 세팅
		Notice notice = new Notice();
		notice.setBoardType(boardType);
		notice.setNoticeTitle(title);
		notice.setNoticeContent(content);
		
		// boardType에 따라 boardCode 설정
		if("notice".equals(boardType)) {
			notice.setBoardCode(4);
		} else if ("faq".equals(boardType)) {
			notice.setBoardCode(5);
		}
		
		// 서비스 호출해서 DB 저장
		int result = service.insertNotice(notice);
		
		if(result>0) {
			ra.addFlashAttribute("message", "공지사항이 등록되었습니다.");
		} else {
			ra.addFlashAttribute("message", "등록에 실패했습니다.");
		}
		
		return "redirect:/notice"; // 공지사항 목록으로 리다이렉트
		
	}
	
	
	
	
	
	
}
