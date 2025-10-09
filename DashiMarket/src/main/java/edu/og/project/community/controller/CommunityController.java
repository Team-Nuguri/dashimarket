package edu.og.project.community.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.common.dto.Comment;
import edu.og.project.community.model.dto.Community;
import edu.og.project.community.model.service.CommunityService;
import edu.og.project.joonggo.model.dto.JoonggoWrite;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import oracle.jdbc.proxy.annotation.Post;

@Controller
public class CommunityController {
	@Autowired
	private CommunityService service;

	// 커뮤니티 목록 조회 (동기)
	@GetMapping("/{boardType:c.*}")
	public String selectCommunityList(@PathVariable("boardType") String boardType,
									  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
									  @RequestParam(value="category", required=false) String category,
									  @RequestParam(value="sort", required=false, defaultValue="latest") String sort,
									  Model model,
									  HttpSession session /* 동네 확인 */) {
		
		Map<String, Object> map = getData(boardType, cp, category, sort, session);
		model.addAllAttributes(map);
		
		return "communityPage/communityHome";
			
	}
	
	// 커뮤니티 목록 조회 (비동기)
	@GetMapping(value="/{boardType:c.*}/filter", produces="application/html; charset=UTF-8")
	public String filterCommunityList(@PathVariable("boardType") String boardType,
									  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
									  @RequestParam(value="category", required=false) String category,
									  @RequestParam(value="sort", required=false, defaultValue="latest") String sort,
									  Model model,
									  HttpSession session /* 동네 확인 */) {
		
		Map<String, Object> map = getData(boardType, cp, category, sort, session);
		model.addAllAttributes(map);
		
		// 프레그먼트 반환
		return "communityPage/communityHome :: #community-list";
		
	}
	
	
	// 동기, 비동기 요청 로직이 같으므로 한 메소드로 빼서 사용 (목록 조회 서비스 호출)
	private Map<String, Object> getData(String boardType, int cp, String category, String sort, HttpSession session) {
		// 세션에 저장된 회원 정보
//		Member loginMember = session.getAttribute("loginMember");
		
		// 선택한 동네값 존재 여부
//		String selectDong = (String) session.getAttribute("selectDong");
//		String finalDong = null;
		
		// 선택한 동네가 없는 경우 회원의 동네로 조회
//		if(selectDong == null) {
//			finalDong = loginMember.getDefaultDong();
//		} else {
//			finalDong = selectDong;
//	}
		// ***************************************************************
		// 회원가입 로그인 로직 구현 후 파라미터 추가 수정 필요
		// ***************************************************************
		return service.selectCommunityList(boardType, cp, category, sort);
	}
	
	// 커뮤니티 상세조회
	@GetMapping("/{boardType:c.*}/{boardNo:C.*}")
	public String communityDetail(@PathVariable("boardType") String boardType,
								  @PathVariable("boardNo") String boardNo,
								  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
								  Model model, RedirectAttributes ra) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("boardType", boardType);
		map.put("boardNo", boardNo);
		
		Community community = service.communityDetail(map);

		String path = null;
		
		if(community != null) {
			path = "communityPage/communityDetail";
			model.addAttribute("board", community);
		} else {
			path = "redirect:/community/" + boardType;
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
		}
		return path;
	}
	
	/************************************************************************************************************/
	/************************************************************************************************************/
	
	// 비동기 댓글 조회
	@GetMapping(value="/comment", produces = "application/html; charset=UTF-8")
	public String selectComment(String boardNo, String sort, Model model) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("boardNo", boardNo);
		map.put("sort", sort);
		
		List<Comment> commentList = service.selectComment(map);
		
		// 동기식과 데이터 타입을 맞추기 위함
		Community community = new Community();
		community.setCommentList(commentList);
		
		model.addAttribute("board", community);
		
		return "/communityPage/communityDetail :: #comment-list-area";
	}
	
	// 댓글 등록
	@PostMapping("/comment/write")
	@ResponseBody
	public int insertComment(@RequestBody Comment comment) {
		return service.insertComment(comment);
	}
	
	// 댓글 수정
	@PutMapping("/comment/update")
	@ResponseBody
	public int updateComment(@RequestBody Comment comment) {
		return service.updateComment(comment);
	}
	
	// 댓글 삭제
	@DeleteMapping("/comment/delete")
	@ResponseBody
	public int deleteComment(@RequestBody Comment comment) {
		return service.deleteComment(comment);
	}
	
	// 커뮤니티 글쓰기 화면 이동
	@GetMapping("/{boardType:c.*}/write")
	public String communityWriteFoward() {
		return "/communityPage/communityWrite";
	}
	
	// 커뮤니티 글쓰기
	@PostMapping("/{boardType:c.*}/write")
	@ResponseBody
	public String communityWrite(Community community,
								 @PathVariable("boardType") String boardType,
								 @RequestParam(value="communityImg", required=false) List<MultipartFile> images
								 //@SessionAttribute("loginMember") Member member 나중에 로그인 완성되면 추가
								) throws IllegalStateException, IOException {
		
		// 임시 회원번호
		community.setMemberNo(3);
		community.setBoardType(boardType);
		
		String result = service.communityWrite(community, images);
		
		System.out.println(result);
		
		result = "/"+ boardType + "/" + result;
		return result;
	}
	
}
