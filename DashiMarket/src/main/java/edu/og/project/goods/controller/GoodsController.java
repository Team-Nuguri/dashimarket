package edu.og.project.goods.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.og.project.goods.model.service.GoodsService;

@Controller
public class GoodsController {
	
	@Autowired
	private GoodsService service;

	// 굿즈 상품 목록 조회
	@GetMapping("/{boardType}")
	public String selectGoodsList(@PathVariable("boardType") String boardType,
								  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
								  Model model) {
		
		Map<String, Object> map = service.selectGoodsList(boardType, cp);
		System.out.println(map);
		
		model.addAttribute("map", map);
		return "goodsPage/goodsHome";
	}
	
    // 파비콘 요청을 명시적으로 처리하여 다른 GetMapping을 타지 않도록 함
    @GetMapping("favicon.ico")
    @ResponseBody // 뷰 리졸버를 거치지 않고 응답을 바로 보냄
    public void returnNoFavicon() {
        // 브라우저에게 아무것도 없다는 의미로 응답을 돌려줌
    }
}
