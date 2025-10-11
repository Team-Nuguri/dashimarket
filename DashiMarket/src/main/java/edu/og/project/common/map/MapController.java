package edu.og.project.common.map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MapController {
	
	// 카카오맵 API 팝업 요청 처리
	@GetMapping("/openMap")
	public String openMap() {
		return "/common/map";
	}
	
	// 선택한 행정동 세션에 올리기
	@PostMapping("/selectDong")
	public String selectDong() {
		return null;
	}

}
