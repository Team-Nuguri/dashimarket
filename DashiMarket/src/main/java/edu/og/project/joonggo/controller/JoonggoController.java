package edu.og.project.joonggo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.joonggo.model.dto.JoonggoWrite;
import edu.og.project.joonggo.model.dto.SimilarItem;
import edu.og.project.joonggo.model.service.JoonggoService;
import edu.og.project.joonggo.model.service.JoonggoServiceImpl;

@Controller
public class JoonggoController {
	
	@Autowired
	private JoonggoService service;
	
	
	// 중고 상품  상세 조회
	@GetMapping("/{boardType}/{joonggoNo:J.*}")
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
	
	
	//중고 상품 등록 페이지 전환
	@GetMapping("/joonggo/write")
	public String joonggoWriteForward() {
		
		return "joonggoPage/joonggoWrite";
		
	}
	
	
	// 중고 상품 등록
	@PostMapping("/{boardType}/write")
	@ResponseBody
	public String joonggoInsert(JoonggoWrite joonggoWrite,
			@PathVariable("boardType") String boardType
			//@SessionAttribute("loginMember") Member member 나중에 로그인 완성되면 추가		
			) throws IllegalStateException, IOException {
		
		joonggoWrite.setMemberNo(1);
		joonggoWrite.setBoardType(boardType);
		
		System.out.println("joonggoWrite :" + joonggoWrite);
		String result = service.joonggoInsert(joonggoWrite);
		
		
		result = "/"+ boardType + "/" + result;
		System.out.println(result);
		return result;
	}
	
	
	
	// 중고 상품 삭제
	@DeleteMapping("/joonggo/delete")
	@ResponseBody
	public int deleteJoonggoItem(
			@RequestBody Map<String, String> paramMap ) {
		
			String joonggoNo = paramMap.get("joonggoNo");
			
			System.out.println(joonggoNo);
		
		
		return service.deleteJoonggoItem(joonggoNo);
	}
		
	

}
