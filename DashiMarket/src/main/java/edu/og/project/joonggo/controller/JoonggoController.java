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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import edu.og.project.common.dto.Member;
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
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
			@SessionAttribute(value ="loginMember" , required = false) Member loginMember,
			Model model
			// 세션에서 로그인 번호 얻어와야 함
			) {
		
		Map<String, Object> map = new HashMap<>();
		
		
		Joonggo joonggo = service.selectJoonggoDetail(joonggoNo);
		
		
		if(joonggo != null) {
			map.put("boardNo", joonggo.getJoonggoNo());
			map.put("categoryId", joonggo.getCategoryId());
			
			List<SimilarItem> similarList = service.selectJonggoList(map);
			
			model.addAttribute("joonggo", joonggo);
			model.addAttribute("similarList", similarList);
			
			// 유저가 좋아요 했는지 확인
			// loginMember.getMemberNo() 이러식으로 바꾸기
			if(loginMember != null) {
				
				map.put("memberNo", loginMember.getMemberNo());
				
				int likeSelect = service.likeSelect(map);
				
				if(likeSelect != 0) {
					model.addAttribute("likeCheck", "like");
				}
			}
		}
		
		
		return "joonggoPage/joonggoDetail";
	}
	
	
	// 중고 찜 상품
	@PostMapping("/joonggo/like")
	@ResponseBody
	public int joonggoLike(
			@RequestBody Map<String, Object> paramMap
			, @SessionAttribute("loginMember") Member loginMember
			) {
		
		paramMap.put("memberNo", loginMember.getMemberNo());
		
		System.out.println(paramMap);
		
		
		return service.joonggoLike(paramMap);
	}
	
	
	
	
	
	
	//중고 상품 등록 페이지 전환
	@GetMapping("/joonggo/write")
	public String joonggoWriteForward() {
		
		return "joonggoPage/joonggoWrite";
		
	}
	
	
	// 중고 상품 등록
	@PostMapping("/{boardType:j.*}/write")
	@ResponseBody
	public String joonggoInsert(JoonggoWrite joonggoWrite,
			@PathVariable("boardType") String boardType,
			@SessionAttribute("loginMember") Member loginMember
			) throws IllegalStateException, IOException {
		
		joonggoWrite.setMemberNo(loginMember.getMemberNo());
		joonggoWrite.setBoardType(boardType);
		
		System.out.println("joonggoWrite :" + joonggoWrite);
		String result = service.joonggoInsert(joonggoWrite);
		
		if(result == null) {
			return "fail";
		}else {
			
			result = "/"+ boardType + "/" + result;
			System.out.println(result);
			return result;
		}
		
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
	
	
	
	// 중고 수정 화면 전환
	@GetMapping("/{boardType}/{joonggoNo:J.*}/update")
	public String joonggoUpdateForward(
			@PathVariable("boardType") String BoardType,
			@PathVariable("joonggoNo") String joonggoNo,
			Model model) {
		
		Joonggo joonggo = service.selectJoonggoDetail(joonggoNo);
		
		model.addAttribute(joonggo);
		
		return "joonggoPage/joonggoUpdate"; 
				
	}
	
	// 중고 수정 처리
	@PostMapping("/{boardType}/{joonggoNo:J.*}/update")
	@ResponseBody
	public String joonggoUpdate(
			@PathVariable("boardType") String boardType
			, @PathVariable("joonggoNo") String joonggoNo
			, @RequestParam(value = "deleteList", required = false) String deleteList
			, @RequestParam(value = "cp", required = false, defaultValue = "1") int cp
			, @ModelAttribute JoonggoWrite joonggoWrite) throws IllegalStateException, IOException{
		
		
		
		joonggoWrite.setJoonggoNo(joonggoNo);
		
		System.out.println(deleteList);
		System.out.println(joonggoWrite.getImageList());
		
		Map<String, Object> map = new HashMap<>();
		map.put("joonggoWrite", joonggoWrite);
		map.put("deleteList", deleteList);
		
		String result = service.joonggoUpdate(map);
		
		return result;
	}
	
	
		
	

}
