package edu.og.project.notice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.og.project.common.dto.Member;
import edu.og.project.notice.model.service.FaqService;

@Controller
public class FaqController {

	@Autowired
	private FaqService service;
	
	@GetMapping("/faq")
	public String faqList(@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value ="query", required = false) String keyword,
			Model model,
			@SessionAttribute(value = "loginMember", required = false) Member loginMember) {
	
	
		return "notice/faq";
	}
}
