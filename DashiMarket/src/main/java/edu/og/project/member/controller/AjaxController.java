package edu.og.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.og.project.member.model.service.AjaxService;

@Controller
public class AjaxController {

	@Autowired
	private AjaxService service;
	
	@GetMapping("/dupCheck/email")
	@ResponseBody
	public int checkEmail(@RequestParam("email") String email) {
		return service.checkEmail(email);
	}
	
	@GetMapping("/dupCheck/nickname")
	@ResponseBody
	public int checkNickname (@RequestParam("memberNickname") String memberNickname) {
		int result = service.checkNickname(memberNickname);
		return result;
	}
	
}
