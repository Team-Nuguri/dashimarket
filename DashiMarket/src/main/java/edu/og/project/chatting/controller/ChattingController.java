package edu.og.project.chatting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.og.project.admin.model.dto.Report;
import edu.og.project.chatting.model.dto.ChattingMessage;
import edu.og.project.chatting.model.dto.ChattingRoom;
import edu.og.project.chatting.model.service.ChattingService;
import edu.og.project.common.dto.Member;
import edu.og.project.joonggo.model.dto.Joonggo;
import jakarta.servlet.http.HttpSession;

@Controller
public class ChattingController {
	
	@Autowired
	private ChattingService service;

	// 채팅 요청
	@GetMapping("/chatting")
	public String chatting(Model model, @SessionAttribute("loginMember") Member loginMember) {
		
		// 채팅방 목록 조회
		List<ChattingRoom> roomList = service.selectRoomList(loginMember.getMemberNo());
		
		model.addAttribute("roomList", roomList);
		
		return "chatting/chatting";
	}
	
	
	// 채팅방 목록 조회
	@GetMapping(value="/chatting/roomList", produces="application/json; charset=UTF-8")
	@ResponseBody
	public List<ChattingRoom> selectRoomList(@SessionAttribute("loginMember") Member loginMember){
		return service.selectRoomList(loginMember.getMemberNo());
	}
	
	// 채팅 상대 검색
	@GetMapping(value="/chatting/selectTarget", produces="application/json; charset=UTF-8")
	@ResponseBody
	public List<Member> selectTarget(@SessionAttribute("loginMember") Member loginMember
			, String query){
		
		Map<String, Object> map = new HashMap<>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("query", query);
		
		return service.selectTarget(map);
	}
	
	// 채팅방 읽음 표시
	@PutMapping("/chatting/updateReadFlag")
	@ResponseBody
	public int chattingRead(@RequestBody Map<String, Object> paramMap) {
		return service.updateReadFlag(paramMap);
	}
	
	
	// 채팅 메세지 내용 목록 조회
	@GetMapping(value="/chatting/selectMessageList", produces="application/json; charset=UTF-8")
	@ResponseBody
	public List<ChattingMessage> selectMessageList(@RequestParam Map<String, Object> paramMap){
		return service.selectMessageList(paramMap);
	}
	
	// 중고상품으로 채팅하기
	@PostMapping("/chatting/enter")
	@ResponseBody
    public int enterJoonggoChat(@RequestBody ChattingRoom chat) {
		
		int chattingNo = service.enterJoonggoChat(chat);
		
        return chattingNo;
    }
	
	// 나가기 -> 채팅방 DeleteFlag 업데이트
	@PostMapping("/chatting/exit")
	@ResponseBody
    public String exitChat(@RequestBody ChattingRoom request, 
    		@SessionAttribute("loginMember") Member loginMember) {
        
		int memberNo = loginMember.getMemberNo();
		
		Map<String, Object> map = new HashMap<>();
		map.put("chattingNo", request.getChattingNo());
		map.put("targetNo", request.getTargetNo());
		map.put("memberNo", memberNo);
		map.put("productNo", request.getProductNo());
		
        boolean result = service.exitChatRoom(map);
        return result ? "success" : "fail";
    }	

	// 신고 후 나가기 -> 신고 테이블에 삽입
    @PostMapping("/chatting/reportExit")
    @ResponseBody
    public String reportAndExit(@RequestBody ChattingRoom request, 
    		@SessionAttribute("loginMember") Member loginMember) {
        
    	int reporterNo = loginMember.getMemberNo();
    	
    	Map<String, Integer> map = new HashMap<>();
		map.put("chattingNo", request.getChattingNo());
		map.put("targetNo", request.getTargetNo());
		map.put("memberNo", reporterNo);
    	
        boolean result = service.reportAndExit(map);
        return result ? "success" : "fail";
    }
	
	
}
