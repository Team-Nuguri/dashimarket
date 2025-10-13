package edu.og.project.notice.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.common.dto.Member;
import edu.og.project.notice.model.dto.Notice;
import edu.og.project.notice.model.service.NoticeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class NoticeController {

	@Autowired
	private NoticeService service;
	
	// config.properties에서 공지사항 경로 읽어오기
	@Value("${my.notice.location}")
    private String noticeLocation;
	
	@Value("${my.notice.webpath}")
    private String noticeWebPath;

	
	// =========== 공지사항 목록 조회 (검색 + 페이징) ===========
	@GetMapping("/notice")
	public String selectNoticeList(@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "query", required = false) String query,
			Model model,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {

		// 페이지당 게시글 수
		int pageSize = 10;

		// 검색어 처리
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("query", query);
		paramMap.put("offset", (currentPage - 1) * pageSize);
		paramMap.put("pageSize", pageSize);

		// 목록 조회
		List<Notice> noticeList = service.selectNoticeList(paramMap);
		// 전체 게시글 수 조회
		int totalCount = service.getNoticeCount(query);
		// 전체 페이지 수 계산
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);

		// 페이지 그룹 계산 (1~10, 11~20, ...)
		int pageGroupSize = 10;
		int currentPageGroup = (currentPage - 1) / pageGroupSize;
		int startPage = currentPageGroup * pageGroupSize + 1;
		int endPage = Math.min(startPage + pageGroupSize - 1, totalPages);
		
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("query", query);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("pageSize", pageSize);

		return "notice/notice";
	}

	// =========== 공지사항 상세 조회 (조회수 증가 포함) ===========
	@GetMapping("/notice/detail/{noticeNo}")
    public String selectNoticeDetail(@PathVariable("noticeNo") int noticeNo,
    		HttpServletRequest request,
    		HttpServletResponse response,
    		Model model) {
    	
    	String cookieName = "noticeView_" + noticeNo;
    	boolean isViewCountUpdated = false;
    	
    	// 쿠키 체크
    	Cookie[] cookies = request.getCookies();
    	if(cookies != null) {
    		for (Cookie c : cookies) {
    			if (cookieName.equals(c.getName())) {
    				// 이미 조회했으면 -> 조회수 증가 제한
    				isViewCountUpdated = true;
    				break;
    			}
    		}
    	}
    	
    	if (!isViewCountUpdated) {
            // 쿠키가 없으면 조회수 증가 및 쿠키 생성
            service.increaseViewCount(noticeNo);

            Cookie newCookie = new Cookie(cookieName, "true");
            newCookie.setMaxAge(60 * 60 * 24); // 1일간 유지
            newCookie.setPath("/"); // 모든 경로에서 유효
            response.addCookie(newCookie);
        }
        
        // 공지사항 상세 조회
        Notice notice = service.selectNoticeDetail(noticeNo);
        
        if(notice == null) {
            return "redirect:/notice";
        }
        
        // 이전글 조회
        Notice prevNotice = service.selectPrevNotice(noticeNo);
        
        // 다음글 조회
        Notice nextNotice = service.selectNextNotice(noticeNo);
        
        model.addAttribute("notice", notice);
        model.addAttribute("prevNotice", prevNotice);
        model.addAttribute("nextNotice", nextNotice);
        
        return "notice/noticeDetail";
    }

	// =========== 관리자 전용 글쓰기 폼 ===========
	@GetMapping("/notice/write")
	public String noticeWriteForm(Model model,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
		
		// 관리자 권한 체크
        if (loginMember == null || !"Y".equals(loginMember.getIsAdmin())) {
            return "redirect:/notice";
        }
		
		model.addAttribute("mode", "write");
		model.addAttribute("notice", new Notice());
		return "notice/notice-adminWrite";
	}

	// =========== 관리자 전용 글쓰기 등록 처리 ===========
	@PostMapping("/notice/write")
	public String noticeWriteSubmit(@RequestParam("admWriteBoardSelect") String boardType,
			@RequestParam("admWriteTitle") String title, 
			@RequestParam("admWriteContent") String content,
			@RequestParam(value = "admWriteFileInput", required = false) MultipartFile file,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember,
			RedirectAttributes ra) {
		
		// 관리자 권한 체크
		if (loginMember == null || !"Y".equals(loginMember.getIsAdmin())) {
            return "redirect:/notice";
        }

		Notice notice = new Notice();
		notice.setBoardType(boardType);
		notice.setNoticeTitle(title);
		notice.setNoticeContent(content);

		if ("notice".equals(boardType)) {
			notice.setBoardCode(4);
		} else if ("faq".equals(boardType)) {
			notice.setBoardCode(5);
		}
		
		// 파일 업로드 처리
        if (file != null && !file.isEmpty()) {
            String savedFileName = saveFile(file);
            if (savedFileName != null) {
                notice.setAttachmentPath(noticeWebPath + savedFileName); // ⭐ 웹 경로 저장
                notice.setAttachmentOriginalName(file.getOriginalFilename());
            }
        }

		
		int result = service.insertNotice(notice);

		if (result > 0) {
			ra.addFlashAttribute("message", "공지사항이 등록되었습니다.");
		} else {
			ra.addFlashAttribute("message", "등록에 실패했습니다.");
		}

		return "redirect:/notice";
	}

	// =========== 공지사항 수정 폼 ===========
	@GetMapping("/notice/edit/{noticeNo}")
	public String noticeEditForm(@PathVariable("noticeNo") int noticeNo,
			Model model,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
		
		// 관리자 권한 체크
		if(loginMember == null || !"Y".equals(loginMember.getIsAdmin())) {
			return "redirect:/notice";
		}
		
		// 기존 게시글 정보 조회
		Notice notice = service.selectNoticeDetail(noticeNo);

		if (notice == null) {
			return "redirect:/notice";
		}
		
		// 모델에 데이터 추가
		model.addAttribute("mode", "edit"); // 수정 모드
		model.addAttribute("notice", notice);
		
		return "notice/notice-adminWrite";
	}

	// =========== 공지사항 수정 처리 ===========
	@PostMapping("/notice/edit/{noticeNo}")
	public String noticeEditSubmit(
			@PathVariable("noticeNo") int noticeNo,
			@RequestParam("admWriteBoardSelect") String boardType, 
			@RequestParam("admWriteTitle") String title,
			@RequestParam("admWriteContent") String content,
			@RequestParam(value = "admWriteFileInput", required = false) MultipartFile file, 
			RedirectAttributes ra,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
		
		// 관리자 권한 체크
	    if (loginMember == null || !"Y".equals(loginMember.getIsAdmin())) {
	        return "redirect:/notice";
	    }
		
		Notice notice = new Notice();	
		notice.setNoticeNo(String.valueOf(noticeNo));
		notice.setBoardType(boardType);
		notice.setNoticeTitle(title);
		notice.setNoticeContent(content);
		
		if ("notice".equals(boardType)) {
			notice.setBoardCode(4);
		} else if ("faq".equals(boardType)) {
			notice.setBoardCode(5);
		}
		
		// 파일 업로드 처리 (새 파일이 있을 경우)
        if (file != null && !file.isEmpty()) {
            String savedFileName = saveFile(file);
            if (savedFileName != null) {
                notice.setAttachmentPath(noticeWebPath + savedFileName); // ⭐ 웹 경로 저장
                notice.setAttachmentOriginalName(file.getOriginalFilename());
            }
        }
		

		int result = service.updateNotice(notice);

		if (result > 0) {
			ra.addFlashAttribute("message", "공지사항이 수정되었습니다.");	
			return "redirect:/notice/detail/" + noticeNo;
		} else {
			ra.addFlashAttribute("message", "수정에 실패했습니다.");
			return "redirect:/notice/edit/" + noticeNo;
		}
	}

	// =========== 공지사항 삭제 처리 (DELETE 메서드로 변경) ===========
		@DeleteMapping("/notice/delete/{noticeNo}")
		@ResponseBody
		public ResponseEntity<?> noticeDelete(
				@PathVariable("noticeNo") int noticeNo,
				@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
			
			// 관리자 권한 체크
	        if (loginMember == null || !"Y".equals(loginMember.getIsAdmin())) {
	        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
	        }

			int result = service.deleteNotice(noticeNo);

			if (result > 0) {
				return ResponseEntity.ok("삭제 성공");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
			}
		}
	
	// =========== 파일 저장 메서드 (중복 제거용 메서드) ===========
    private String saveFile(MultipartFile file) {
        try {
            // 업로드 디렉토리 생성
            File uploadDir = new File(noticeLocation);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 파일명 중복 방지 (UUID 사용)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String savedFileName = UUID.randomUUID().toString() + extension;

            // 파일 저장 (실제 파일 시스템 경로에 저장)
            Path filePath = Paths.get(noticeLocation + savedFileName);
            Files.write(filePath, file.getBytes());

            return savedFileName; // 파일명만 반환
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
