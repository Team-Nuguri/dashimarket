package edu.og.project.goods.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.og.project.goods.model.dto.Goods;
import edu.og.project.goods.model.service.GoodsService;

@Controller
public class GoodsController {
	
	@Autowired
	private GoodsService service;

	// 굿즈 상품 목록 조회
	@GetMapping("/{boardType:g.*}")
	public String selectGoodsList(@PathVariable("boardType") String boardType,
								  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
								  Model model) {
		
		Map<String, Object> map = service.selectGoodsList(boardType, cp);
		System.out.println(map);
		
		model.addAttribute("map", map);
		return "goodsPage/goodsHome";
	}
	
	// 굿즈 정렬 (비동기)
	@GetMapping(value="/{boardType:g.*}/sort", produces="application/html; charset=UTF-8") /*json이 아닌 html 형태로 보내주기*/
	public String sortGoodsList(@PathVariable("boardType") String boardType,
								     @RequestParam(value="cp", required=false, defaultValue="1") int cp,
								     Model model,
								     @RequestParam("sortType") String sortType) {
		// 정렬 방식 넘겨주기
		Map<String, Object> resultMap = service.sortGoodsList(boardType, cp, sortType);
		model.addAllAttributes(resultMap); // return에 바로 resultMap을 담지 않고, model로 넘겨줌
		
		return "goodsPage/goodsHome :: #goods-container"; // 동적으로 바뀔 부분의 아이디
	}
	
    // 파비콘 요청 막기
    @GetMapping("favicon.ico")
    @ResponseBody // 뷰 리졸버를 거치지 않고 응답을 바로 보냄
    public void returnNoFavicon() {
        // 브라우저에게 아무것도 없다는 의미로 응답을 돌려줌
    }
}
