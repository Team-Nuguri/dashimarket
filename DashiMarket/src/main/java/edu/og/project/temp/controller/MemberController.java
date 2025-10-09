package edu.og.project.temp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.temp.model.Member;
import edu.og.project.temp.model.MemberService;

@Controller
@SessionAttributes("loginMember")
public class MemberController {
	
	@Autowired
	private MemberService service;
	
	// 로그인 화면 이동
	@GetMapping("/login")
	public String login() {
		return "temp/logIn";
	}
	
	// 로그인 요청 처리
	@PostMapping("/login")
	public String login(Model model, Member inputMember
			, @RequestHeader(value="referer", required=false) String referer
			, RedirectAttributes ra) {
		
		Member loginMember = service.login(inputMember);
		
		String path = "redirect:";
		
		if(loginMember != null) {
			path += "/";
			model.addAttribute("loginMember", loginMember);
		
		}else {
			path += referer;
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		return path;
	}

}
