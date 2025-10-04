package edu.og.project.community.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.og.project.community.model.service.CommunityService;

@Controller
public class CommunityController {
	@Autowired
	private CommunityService service;

	// 커뮤니티 목록 조회
	@GetMapping("/{boardType:c.*}")
	public String selectCommunityList(@PathVariable("boardType") String boardType,
									  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
									  Model model) {
		
		Map<String, Object> map = service.selectCommunityList(boardType, cp);
		model.addAttribute("map", map);
		
		return "communityPage/communityHome";
	}
	
	// 커뮤니티 정렬 (비동기)
	@GetMapping(value="/{boardType:c.*}/sort", produces="application/html; charset=UTF-8") /*json이 아닌 html 형태로 보내주기*/
	public String sortCommunityList(@PathVariable("boardType") String boardType,
								     @RequestParam(value="cp", required=false, defaultValue="1") int cp,
								     Model model,
								     @RequestParam("sortType") String sortType) {
		// 정렬 방식 넘겨주기
		Map<String, Object> resultMap = service.sortCommunityList(boardType, cp, sortType);
		model.addAllAttributes(resultMap); // return에 바로 resultMap을 담지 않고, model로 넘겨줌
		
		return "goodsPage/goodsHome :: #community-list"; // 동적으로 바뀔 부분의 아이디
	}
}
