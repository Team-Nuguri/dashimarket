package edu.og.project.joonggo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.joonggo.model.dto.SimilarItem;
import edu.og.project.joonggo.model.service.JoonggoService;
import edu.og.project.joonggo.model.service.JoonggoServiceImpl;

@Controller
public class JoonggoController {
	
	@Autowired
	private JoonggoService service;
	
	
	// 중고 상품  상세 조회
	@GetMapping("/{boardType}/{joonggoNo}")
	public String selectJoonggoDetail(
			@PathVariable("joonggoNo") String joonggoNo,
			@PathVariable("boardType") String boardType,
			Model model
			) {
		
		Map<String, Object> map = new HashMap<>();
		
		
		Joonggo joonggo = service.selectJoonggoDetail(joonggoNo);
		
		
		if(joonggo != null) {
			map.put("boardNo", joonggo.getJoonggoNo());
			map.put("categoryId", joonggo.getCategoryId());
			
			List<SimilarItem> similarList = service.selectJonggoList(map);
			
			model.addAttribute("joonggo", joonggo);
			model.addAttribute("similarList", similarList);
		}
		
		
		return "joonggoPage/joonggoDetail";
	}
		
	

}
