package edu.og.project.community.controller;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Member;
import edu.og.project.community.model.dto.Community;
import edu.og.project.community.model.service.CommunityService;
import edu.og.project.joonggo.model.dto.JoonggoWrite;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
									  @SessionAttribute(value="selectDong", required=false) String selectDong, /* 동네 확인 */
									  @SessionAttribute(value="loginMember", required=false) Member loginMember) {
		
									// 공통 메소드
		Map<String, Object> map = getData(boardType, cp, category, sort, selectDong, loginMember, model);
		model.addAllAttributes(map);
		
		return "/communityPage/communityHome";
			
	}
	
	// 커뮤니티 목록 조회 (비동기)
	@GetMapping(value="/{boardType:c.*}/filter", produces="application/html; charset=UTF-8")
	public String filterCommunityList(@PathVariable("boardType") String boardType,
									  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
									  @RequestParam(value="category", required=false) String category,
									  @RequestParam(value="sort", required=false, defaultValue="latest") String sort,
									  Model model,
									  @SessionAttribute(value="selectDong", required=false) String selectDong, /* 동네 확인 */
									  @SessionAttribute(value="loginMember", required=false) Member loginMember) {
		
									// 공통 메소드
		Map<String, Object> map = getData(boardType, cp, category, sort, selectDong, loginMember, model);
		model.addAllAttributes(map);
		
		// 프레그먼트 반환
		return "communityPage/communityHome :: #community-list";
		
	}
	
	// 관심있는 게시글 조회
	@GetMapping(value="/{boardType:c.*}/likeLists")
	public String selectLikeCommunityList(@PathVariable("boardType") String boardType,
										  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
										  @SessionAttribute("loginMember") Member loginMember,
										  Model model) {
		
		// 로그인한 회원의 번호
		int memberNo = loginMember.getMemberNo();
		Map<String, Object> map = service.selectLikeCommunityList(boardType, memberNo, cp);
		model.addAttribute("map", map);
		return "/communityPage/communityLike";
	}
	
	
	// 동기, 비동기 요청 로직이 같으므로 한 메소드로 빼서 사용 (목록 조회 서비스 호출)
	private Map<String, Object> getData(String boardType, int cp, String category, String sort, String selectDong, Member loginMember, Model model) {
		
		// 선택된 동네값 최종 저장 변수
		String finalDong = null;
		
		// 세션에 selectDong이 있는 경우 (맵 선택 or 현재위치)
		if(selectDong != null && !selectDong.trim().isEmpty()) {
			finalDong = selectDong; // 세션에 있는 동네값으로 세팅
		} 
		
		// 로그인만 한 상태인 경우(맵 선택 x, 세션에 동네 값 없음)
		else if(loginMember != null) {
			String hDongStr = loginMember.getJibunAddress();
			String hDongArr[] = hDongStr.split(" ");
			finalDong = hDongArr[hDongArr.length - 2];
		}
		
		int loginMemberNo;
		
		// 로그인이 되어있지 않다면 회원 번호를 가져올 수 없음
		if(loginMember != null) {
			loginMemberNo = loginMember.getMemberNo();
		} else {
			// 로그인이 되어있지 않은 경우 0 반환 => 0인 경우 목록 조회시 좋아요 여부 확인 x
			loginMemberNo = 0;
		}
		
		System.out.println("최종 조회 동네 : " + finalDong);
		model.addAttribute("finalDong", finalDong);
		return service.selectCommunityList(loginMemberNo, boardType, cp, category, sort, finalDong);
	}
	
	// 커뮤니티 상세조회
	@GetMapping("/{boardType:c.*}/{boardNo:C.*}")
	public String communityDetail(@PathVariable("boardType") String boardType,
								  @PathVariable("boardNo") String boardNo,
								  @RequestParam(value="cp", required=false, defaultValue="1") int cp,
								  @SessionAttribute(value="loginMember", required=false) Member loginMember,
								  Model model, RedirectAttributes ra,
								  
								  /* 쿠키를 이용한 조회수 증가시 사용*/
								  HttpServletRequest req,
								  HttpServletResponse resp) throws ParseException {
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("boardType", boardType);
		map.put("boardNo", boardNo);
		
		Community community = service.communityDetail(map);

		String path = null;
		
		// 조회 결과가 있는 경우
		if(community != null) {
			
			// 로그인한 경우 해당 게시글에 좋아요 클릭 여부
			if(loginMember != null) {
				map.put("memberNo", loginMember.getMemberNo());
				
				// 좋아요 여부 확인 서비스
				int result = service.communityLikeCheck(map);
				
				// 좋아요 누른 적이 있는 경우
				if(result > 0) model.addAttribute("likeCheck", "yes");
			}
			
			// 쿠키를 이용한 조회수 처리
			// 비회원 또는 현재 계정의 게시글이 아닌 경우
			if(loginMember == null || loginMember.getMemberNo() != community.getMemberNo()) {
				
				// 쿠키 얻어오기
				Cookie c = null;
				
				// 요청에 담겨있는 모든 쿠키
				Cookie[] cookies = req.getCookies();
				
				// 순회하며 쿠키 찾기
				for(Cookie cookie : cookies) {
					if(cookie.getName().equals("readCommunityNo")) {
						// readCommunityNo 쿠키를 찾아 대입
						c = cookie;
						break;
					} // end of if
				} // end of for
				
				// 기존에 쿠키가 없는 경우
				// 또는 쿠키는 있지만 현재 보고 있는 게시글 번호가 쿠키에 저장되지 않은 경우
				int result = 0;
				
				// 순회하며 readCommunityNo를 찾았을 때 없는 경우(기존 쿠키가 없는 경우)
				// -> 새로 생성 + 조회수 증가
				if(c == null) {
					c = new Cookie("readCommunityNo", "|" + boardNo + "|");
					
					// 조회수 증가
					result = service.updateReadCount(boardNo);
					
				} else { // 쿠키가 존재하는 경우
					// 현재 게시글 번호가 쿠키에 있는지 확인
					// 쿠키에 없다면 (-1 반환 받음)
					if(c.getValue().indexOf("|" + boardNo + "|") == -1) {
						// 기존 쿠키 값에 게시글 번호 추가(누적)해서 세팅
						c.setValue(c.getValue() + "|" + boardNo + "|");
						
						// 조회수 증가
						result = service.updateReadCount(boardNo);
					}
				}
				
				// 조회수 증가 성공시 쿠키 적용 경로 및 수명(당일 23시 59분 59초) 세팅
				if(result == 1) {
					// 조회된 게시글의 조회수와 DB의 조회수 동기화
					community.setPostViews(community.getPostViews() + 1);
					
					// 쿠키 경로
					c.setPath("/");
					
					// 수명
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 1); // 1일
					
					// 날짜 표기법 변경
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					
					Date current = new Date(); // 현재 시간
					Date temp = new Date(cal.getTimeInMillis()); // 24시간 후(내일
					Date tmr = sdf.parse( sdf.format(temp) ); // 내일 0시 0분 0초
					
					// 내일 0시 0분 0초 - 현재 시간 = 쿠키 수명
					long diff = (tmr.getTime() - current.getTime()) / 1000;
					// 남은 시간을 초단위로 반환
					
					// 쿠키 수명 설정
					c.setMaxAge((int)diff);
					resp.addCookie(c);
				}
				
				
				
			} // 조회수 처리 끝
			
		}
		
		if(community != null) {
			path = "communityPage/communityDetail";
			
			String hDongStr = community.getJibunAddress();
			String hDongArr[] = hDongStr.split(" ");
			String memberHdong = hDongArr[hDongArr.length - 2];
			
			model.addAttribute("board", community);
			model.addAttribute("memberHdong", memberHdong);
			
		} else {
			path = "redirect:/community/" + boardType;
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
		}
		return path;
	}
	
	// 좋아요 처리
	@PostMapping("/community/like")
	@ResponseBody
	public int communityLike(@RequestBody Map<String, Object> paramMap) {
		return service.communityLike(paramMap);
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
								 @RequestParam(value="communityImg", required=false) List<MultipartFile> images,
								 @SessionAttribute("loginMember") Member member
								) throws IllegalStateException, IOException {
		
		community.setMemberNo(member.getMemberNo());
		community.setBoardType(boardType);
		
		String result = service.communityWrite(community, images);
		
		result = "/"+ boardType + "/" + result;
		return result;
	}
	
	// 커뮤니티 게시글 수정 화면 전환
	@GetMapping("/{boardType:c.*}/{boardNo:C.*}/update")
	public String communityUpdate(@PathVariable("boardType") String boardType,
			  					  @PathVariable("boardNo") String boardNo,
			  					  Model model) {
		
		// 상세조회 그대로 갖다 쓰기
		Map<String, Object> map = new HashMap<>();
		map.put("boardType", boardType);
		map.put("boardNo", boardNo);
		
		Community community = service.communityDetail(map);
		
		// forward로 상세 조회 넘기기
		model.addAttribute("community", community);
		
		return "/communityPage/communityUpdate";
	}
	
	// 커뮤니티 게시글 수정 처리
	@PostMapping("/{boardType:c.*}/{boardNo:C.*}/update")
	@ResponseBody
	public String communityUpdate(@PathVariable("boardType") String boardType,
								  @PathVariable("boardNo") String boardNo,
								  @RequestParam(value="cp", required=false, defaultValue="1") String cp,
								  @RequestParam(value="deleteList", required=false) String deleteList,
								  @RequestParam(value="communityImg", required=false) List<MultipartFile> images,
								  Community community,
								  RedirectAttributes ra,
								  HttpSession session,
								  Model model) throws IllegalStateException, IOException {
		
		// 게시글 번호 세팅(커멘트에는 제목과 내용, 카테고리만 담겨있음)
		community.setCommunityNo(boardNo);
		
		return service.communityUpdate(community, deleteList, images);
	}
	
	// 커뮤니티 게시글 삭제
	@GetMapping("/{boardType:c.*}/{boardNo:C.*}/delete")
	public String communityDelete(@PathVariable("boardType") String boardType,
			  					  @PathVariable("boardNo") String boardNo,
			  					  @RequestParam(value="cp", required=false, defaultValue="1") String cp,
								  RedirectAttributes ra,
								  @RequestHeader("referer") String referer /*이전요청주소*/) {
		
		int result = service.communityDelete(boardNo);
		
		String message = "";
		String path = "redirect:";
		
		// 삭제 성공시
		if(result > 0) {
			message = "게시글이 삭제 되었습니다!";
			path += "/" + boardType;
		} else {
			
			message = "게시글 삭제에 실패했습니다.";
			path += referer;
		}
		
		ra.addFlashAttribute("message", message);
		return path;
	}
	
}
