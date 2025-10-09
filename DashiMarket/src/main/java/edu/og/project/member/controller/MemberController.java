package edu.og.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.common.dto.Member;
import edu.og.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/member")
@SessionAttributes("loginMember")
public class MemberController {
	
	@Autowired
	private MemberService service;
	
	@GetMapping("/login")
	public String login(@CookieValue(value="saveId", required=false) String saveId,
	                    Model model) {
	    
	    if(saveId != null) {
	        model.addAttribute("saveId", saveId);
	    }
	    
	    return "member/login";
	}
	
	@PostMapping("/login")
	public String login(Member inputMember, Model model
					, @RequestHeader(value="referer", required=false) String referer
					, @RequestParam(value="saveId", required=false) String saveId					
					, HttpServletResponse resp
					, RedirectAttributes ra) {
		
		Member loginMember = service.login(inputMember);
	
		
		String path = "redirect:";
		
		if(loginMember != null) {
			model.addAttribute("loginMember", loginMember);
			path += "/";
			
	        System.out.println("loginMember:" + loginMember);
	        System.out.println("saveId 체크박스 값: " + saveId);  // ⭐ 추가			

	        Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
		
			if(saveId != null) {
			cookie.setMaxAge(60 * 60 * 24 * 30);
			System.out.println("쿠키 생성: " + loginMember.getMemberEmail());
			   
			}else {
			cookie.setMaxAge(0);
			System.out.println("쿠키 삭제"); 
			
			}
			cookie.setPath("/");
		
			resp.addCookie(cookie);
		
		}else {
			path += referer;
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		return path;
	}
	
	@GetMapping("/logout")
	public String lotout(SessionStatus status) {
		status.setComplete();
		
		return "redirect:/";
	}
	
	@GetMapping("/signUp")
	public String signUp() {
		return "member/signUp";
	}
	
	@PostMapping("/signUp")
	public String signUp(Member inputMember, 
						RedirectAttributes ra) {
	
		
		int result = service.signUp(inputMember);
		
		String path = "redirect:";
		String message = null; 	
		
		if(result > 0) {
			path += "/member/login";
			
			message = inputMember.getMemberNickname() + "님의 가입을 환영합니다.";
		}else {
			path += "/member/signUp";
			
			message = "회원 가입 실패";
		}
		
		ra.addFlashAttribute("message", message); 
					
		return path;
	
		
	}
	
	// 로그인 필터 에러 메시지
	@GetMapping("/loginError")
	public String loginError() {
	    return "member/loginError"; 
	}
	
	

}
