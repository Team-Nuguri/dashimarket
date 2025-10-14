package edu.og.project.joonggo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.joonggo.model.dto.JoonggoWrite;
import edu.og.project.joonggo.model.dto.SimilarItem;
import edu.og.project.joonggo.model.service.JoonggoService;
import edu.og.project.joonggo.model.service.JoonggoServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class JoonggoController {
	
	@Autowired
	private JoonggoService service;
	
	// 중고 상품 목록 조회 (KJK)
	// @GetMapping("/{boardType:j.*}")
	 @GetMapping("/joonggo")
	public String selectJoonggoList( 
			// @PathVariable("boardType") String boardType,
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {
		
		// System.out.println(boardType);  joonggo 로 넘어옴
		// Map<String, Object> map = service.selectJoonggoList(boardType, cp);  // 사용X
	    Map<String, Object> map = service.selectJoonggoList("joonggo", cp);
		
		if(map == null) {
		    System.out.println("map is null");
		} else if(map.isEmpty()) {
		    System.out.println("map is empty");
		} else {
		    System.out.println("map = " + map);
		}
		
		System.out.println(map);

		model.addAttribute("map", map);
		// model.addAttribute("boardType", boardType);
		return "joonggoPage/joonggoHome";
		
	}

	// 중고상품 목록 정렬 (비동기) (KJK)
	@GetMapping(value = "/{boardType:j.*}/sort", produces = "application/html; charset=UTF-8") /*
	 * json이 아닌 html 형태로
	 * 보내주기
	 */
	public String sortJoonggoList(@PathVariable("boardType") String boardType,
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model,
			@RequestParam("sortType") String sortType) {
		// 정렬 방식 넘겨주기
		Map<String, Object> resultMap = service.sortJoonggoList(boardType, cp, sortType);
		model.addAllAttributes(resultMap); // return에 바로 resultMap을 담지 않고, model로 넘겨줌

		return "joonggoPage/joonggoHome :: #joonggo-container"; // 동적으로 바뀔 부분의 아이디
	}
	
	 // 중고상품 카테고리(대분류) 목록 조회 (KJK)
		@GetMapping("/joonggoCategory")
		public String selectJoonggoCategoryList(
				@RequestParam(name = "categoryId", required = false) String categoryId,
				@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
				Model model) {
			
			System.out.println("temp");
			
			Map<String, Object> map = service.selectJoonggoCategoryList(categoryId, cp);
			model.addAttribute("map", map);
			model.addAttribute("categoryId", categoryId);
			
			return "joonggoPage/joonggoHome";
		}
		
		
		 // 중고상품 카테고리(중분류) 목록 조회 (KJK)
		@GetMapping("/joonggoCategory2")
		public String selectJoonggoCategoryList2(
				@RequestParam(name = "categoryId", required = false) String categoryId,
				@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
				Model model) {
			
			System.out.println("temp2");
			
			Map<String, Object> map = service.selectJoonggoCategoryList2(categoryId, cp);
			model.addAttribute("map", map);
			model.addAttribute("categoryId", categoryId);
			
			return "joonggoPage/joonggoHome";
		}
	 
	
	
	// 로그인한 회원의 나의 위시리스트 (KJK)
	@GetMapping("/myPage/wishlist")
	public String wishListPage(Model model, @AuthenticationPrincipal Member loginMember) {

	    List<Joonggo> wishList = service.selectJoonggoWishList(loginMember.getMemberNo());
	    model.addAttribute("wishList", wishList);

	    return "myPage/myPage-wishlist";
	}

	
	
	// 중고 상품  상세 조회
	@GetMapping("/{boardType}/{joonggoNo:J.*}")
	public String selectJoonggoDetail(
			@PathVariable("joonggoNo") String joonggoNo,
			@PathVariable("boardType") String boardType,
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
			@SessionAttribute(value ="loginMember" , required = false) Member loginMember,
			Model model,
			HttpServletRequest req,
			HttpServletResponse resp
			) throws ParseException {
		
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
			
			// 조회수 처리
			if(loginMember == null || loginMember.getMemberNo() != joonggo.getMemberNo()) {
				
				Cookie c = null;
				
				// 모든 쿠키 배열에 담고
				Cookie[] cookies = req.getCookies();
				
				// 배열 순회해서 readJoonggo 쿠키 찾아 있다면 c 에 저장
				for (Cookie cookie : cookies) {
					if(cookie.getName().equals("readJoonggo")) {
						
						c = cookie;
						break;
					}
					
				}
				
				int result = 0;
				
				if(c == null) {
					
					c= new Cookie("readJoonggo", "|" + joonggoNo + "|");
					
					// 조회수 증가 서비스 호출
					result = service.updateReadCount(joonggoNo);
				}else {
					// 쿠키 존재할 때 
					if(c.getValue().indexOf("|" + joonggoNo + "|") == -1) {
						c.setValue(c.getValue()+ "|" + joonggoNo + "|");
						
						result = service.updateReadCount(joonggoNo);
					}
				}
				
				if(result == 1) {
					
					joonggo.setReadCount(joonggo.getReadCount() + 1);
					
					c.setPath("/");
					
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 1); // 1일
					
					// 날짜 표기법 변경
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					
					Date current = new Date(); // 현재 시간
					Date temp = new Date(cal.getTimeInMillis()); // 24시간 후(내일
					Date tmr = sdf.parse( sdf.format(temp) ); // 내일 0시 0분 0초
					
					// 내일 0시 0분 0초 - 현재 시간 = 쿠키 수명
					long lifeTime = (tmr.getTime() - current.getTime()) / 1000;
					// 남은 시간을 초단위로 반환
					
					// 쿠키 수명 설정
					c.setMaxAge((int)lifeTime);
					resp.addCookie(c);
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
	
	// 중고 페이지에서 헤더 검색 - 게시글 제목
	@GetMapping("/joonggo/search")
	@ResponseBody
	public List<Joonggo> joonggoSearch(@RequestParam String query){
		return service.joonggoSearch(query);
	}	
	

}
