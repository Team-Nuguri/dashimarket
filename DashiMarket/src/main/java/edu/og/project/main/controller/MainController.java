package edu.og.project.main.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.og.project.main.service.MainService;

@Controller
public class MainController {
	
	@Autowired
	private MainService service;
	
	// intro 화면 요청
	@RequestMapping("/")
	public String mainForward() {
		return "intro";
	}
	
	// intro 검색 - 모든 게시글 제목으로 검색
	@GetMapping(value="introSearch", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<Map<String, Object>> introSearch(String query){
		return service.introSearch(query);
	}

}
