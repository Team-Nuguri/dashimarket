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

import edu.og.project.common.dto.Image;
import edu.og.project.common.dto.Member;
import edu.og.project.notice.model.dto.Faq;
import edu.og.project.notice.model.service.FaqService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FaqController {

	@Autowired
	private FaqService service;
	
	@Value("${my.notice.location}")
	private String faqLocation;
	
	@Value("${my.notice.webpath}")
	private String faqWebPath;
	
	// =========== FAQ 목록 조회 (검색 + 페이징) ===========
	@GetMapping("/faq")
	public String selectFaqList(
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "query", required = false) String query,
			Model model,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
		
		int pageSize = 10;
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("query", query);
		paramMap.put("offset", (currentPage - 1) * pageSize);
		paramMap.put("pageSize", pageSize);
		
		List<Faq> faqList = service.selectFaqList(paramMap);
		int totalCount = service.getFaqCount(query);
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);
		
		int pageGroupSize = 10;
		int currentPageGroup = (currentPage - 1) / pageGroupSize;
		int startPage = currentPageGroup * pageGroupSize + 1;
		int endPage = Math.min(startPage + pageGroupSize - 1, totalPages);
		
		model.addAttribute("faqList", faqList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("query", query);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("pageSize", pageSize);
		
		return "notice/faq";
	}
	
	// =========== FAQ 상세 조회 ===========
	@GetMapping("/faq/detail/{faqNo}")
	public String selectFaqDetail(
			@PathVariable("faqNo") int faqNo,
			Model model) {
		
		Faq faq = service.selectFaqDetail(faqNo);
		
		if (faq == null) {
			return "redirect:/faq";
		}
		
		model.addAttribute("faq", faq);
		return "notice/faqDetail";
	}
	
	// =========== 관리자 전용 글쓰기 폼 ===========
	@GetMapping("/faq/write")
	public String faqWriteForm(
			Model model,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
		
		if (loginMember == null || !"Y".equals(loginMember.getIsAdmin())) {
			return "redirect:/faq";
		}
		
		model.addAttribute("mode", "write");
		model.addAttribute("faq", new Faq());
		return "notice/faq-adminWrite";
	}
	
	// =========== 관리자 전용 글쓰기 등록 처리 ===========
	@PostMapping("/faq/write")
	public String faqWriteSubmit(
			@RequestParam("admWriteTitle") String title,
			@RequestParam("admWriteContent") String content,
			@RequestParam(value = "admWriteFileInput", required = false) MultipartFile file,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember,
			RedirectAttributes ra) {
		
		if (loginMember == null || !"Y".equals(loginMember.getIsAdmin())) {
			return "redirect:/faq";
		}
		
		Faq faq = new Faq();
		faq.setBoardType("faq");
		faq.setFaqTitle(title);
		faq.setFaqContent(content);
		faq.setBoardCode(5);
		
		int result = service.insertFaq(faq);
		
		if (result > 0 && file != null && !file.isEmpty()) {
			String savedFileName = saveFile(file);
			if (savedFileName != null) {
				Image image = new Image();
				image.setImagePath(faqWebPath + savedFileName);
				image.setImageRename(savedFileName);
				image.setImageOrder(0);
				image.setBoardNo(faq.getFaqNo());
				
				service.insertImage(image);
			}
		}
		
		if (result > 0) {
			ra.addFlashAttribute("message", "FAQ가 등록되었습니다.");
		} else {
			ra.addFlashAttribute("message", "등록에 실패했습니다.");
		}
		
		return "redirect:/faq";
	}
	
	// =========== FAQ 수정 폼 ===========
	@GetMapping("/faq/edit/{faqNo}")
	public String faqEditForm(
			@PathVariable("faqNo") int faqNo,
			Model model,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
		
		if (loginMember == null || !"Y".equals(loginMember.getIsAdmin())) {
			return "redirect:/faq";
		}
		
		Faq faq = service.selectFaqDetail(faqNo);
		
		if (faq == null) {
			return "redirect:/faq";
		}
		
		model.addAttribute("mode", "edit");
		model.addAttribute("faq", faq);
		return "notice/faq-adminWrite";
	}
	
	// =========== FAQ 수정 처리 ===========
	@PostMapping("/faq/edit/{faqNo}")
	public String faqEditSubmit(
			@PathVariable("faqNo") int faqNo,
			@RequestParam("admWriteTitle") String title,
			@RequestParam("admWriteContent") String content,
			@RequestParam(value = "admWriteFileInput", required = false) MultipartFile file,
			RedirectAttributes ra,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
		
		if (loginMember == null || !"Y".equals(loginMember.getIsAdmin())) {
			return "redirect:/faq";
		}
		
		Faq faq = new Faq();
		faq.setFaqNo(String.valueOf(faqNo));
		faq.setBoardType("faq");
		faq.setFaqTitle(title);
		faq.setFaqContent(content);
		faq.setBoardCode(5);
		
		int result = service.updateFaq(faq);
		
		if (result > 0 && file != null && !file.isEmpty()) {
			service.deleteImagesByBoardNo(String.valueOf(faqNo));
			
			String savedFileName = saveFile(file);
			if (savedFileName != null) {
				Image image = new Image();
				image.setImagePath(faqWebPath + savedFileName);
				image.setImageRename(savedFileName);
				image.setImageOrder(0);
				image.setBoardNo(String.valueOf(faqNo));
				
				service.insertImage(image);
			}
		}
		
		if (result > 0) {
			ra.addFlashAttribute("message", "FAQ가 수정되었습니다.");
			return "redirect:/faq";
		} else {
			ra.addFlashAttribute("message", "수정에 실패했습니다.");
			return "redirect:/faq/edit/" + faqNo;
		}
	}
	
	// =========== FAQ 삭제 처리 ===========
	@DeleteMapping("/faq/delete/{faqNo}")
	@ResponseBody
	public ResponseEntity<?> faqDelete(
			@PathVariable("faqNo") int faqNo,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
		
		if (loginMember == null || !"Y".equals(loginMember.getIsAdmin())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
		}
		
		int result = service.deleteFaq(faqNo);
		
		if (result > 0) {
			return ResponseEntity.ok("삭제 성공");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
		}
	}
	
	// =========== 파일 저장 메서드 ===========
	private String saveFile(MultipartFile file) {
		try {
			File uploadDir = new File(faqLocation);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
			
			String originalFilename = file.getOriginalFilename();
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			String savedFileName = UUID.randomUUID().toString() + extension;
			
			Path filePath = Paths.get(faqLocation + savedFileName);
			Files.write(filePath, file.getBytes());
			
			return savedFileName;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// =========== FAQ 아코디언 클릭 시 조회수 증가 ===========
	@PostMapping("/faq/increaseView/{faqNo}")
	@ResponseBody
	public ResponseEntity<?> increaseViewCount(
	        @PathVariable("faqNo") int faqNo,
	        HttpServletRequest request,
	        HttpServletResponse response) {
	    
	    String cookieName = "faqView_" + faqNo;
	    boolean alreadyViewed = false;
	    
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie c : cookies) {
	            if (cookieName.equals(c.getName())) {
	                alreadyViewed = true;
	                break;
	            }
	        }
	    }
	    
	    if (!alreadyViewed) {
	        service.increaseViewCount(faqNo);
	        
	        Cookie newCookie = new Cookie(cookieName, "true");
	        newCookie.setMaxAge(60 * 60 * 24);
	        newCookie.setPath("/");
	        response.addCookie(newCookie);
	        
	        return ResponseEntity.ok("조회수 증가");
	    }
	    
	    return ResponseEntity.ok("이미 조회");
	}

}
