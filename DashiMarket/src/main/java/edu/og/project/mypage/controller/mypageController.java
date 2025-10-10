package edu.og.project.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.og.project.mypage.model.mypageService;

@Controller
@RequestMapping("/myPage")
public class mypageController {
	
	@Autowired
	private mypageService service;
	
	@GetMapping("/profile")
	public String mypageProfile () {
		return "myPage/myPage-profile";
	}
		
	@GetMapping("/fraud")
	public String fraudService () {
		
		return "myPage/fraudService";
	}
}
