package edu.og.project.common.map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class MapController {
	
	// 카카오맵 API 팝업 요청 처리
	@GetMapping("/openMap")
	public String openMap() {
		return "/common/map";
	}
	
	// 선택한 행정동 세션에 올리기
	@PostMapping("/selectDong")
	@ResponseBody
	public String setSessionDong(@RequestBody String selectDong, HttpSession session) {
		
		// 공백 깔끔하게 제거 후 다시 저장해서 세션에 세팅
		String sessionDong = selectDong.trim();
		
		System.out.println(sessionDong);
		
		session.setAttribute("selectDong", sessionDong);
		return sessionDong;
	}

}
