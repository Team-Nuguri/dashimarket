package edu.og.project.community.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.og.project.community.model.service.CommunityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CommunityController {
	@Autowired
	private CommunityService service;

	// 커뮤니티 목록 조회 (카테고리 및 정렬 비동기까지 한 번에 받아서 처리)
	@GetMapping("/{boardType:c.*}")
	public String selectCommunityList(@PathVariable("boardType") String boardType,
									  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
									  @RequestParam(value="category", required=false) String category,
									  @RequestParam(value="sortType", required=false, defaultValue="latest") String sortType,
									  Model model,
									  HttpSession session, /* 동네 확인 */
									  HttpServletRequest fetchHeader /* 비동기 요청에서 가져온 헤더값(비동기 여부 판단) */) {
		
		// 세션에 저장된 회원 정보
//		Member loginMember = session.getAttribute("loginMember");
		
		// 선택한 동네값 존재 여부
//		String selectDong = (String) session.getAttribute("selectDong");
		String finalDong = null;
		
		// 선택한 동네가 없는 경우 회원의 동네로 조회
//		if(selectDong == null) {
//			finalDong = loginMember.getDefaultDong();
//		} else {
//			finalDong = selectDong;
//	}
		// ***************************************************************
		// 회원가입 로그인 로직 구현 후 파라미터 추가 수정 필요
		// ***************************************************************
		Map<String, Object> map = service.selectCommunityList(boardType, cp, category, sortType);
		model.addAttribute("map", map);
		
		return "communityPage/communityHome";
	}
}
