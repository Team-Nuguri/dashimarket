package edu.og.project.chatting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.og.project.chatting.model.dto.ChattingRoom;
import edu.og.project.chatting.model.service.ChattingService;
import edu.og.project.temp.model.Member;

@Controller
public class ChattingController {
	
	@Autowired
	private ChattingService service;

	// 채팅 요청
	@GetMapping("/chatting")
	public String chatting(Model model, @SessionAttribute("loginMember") Member loginMember
			/*임시 - Member DTO 변경하기*/) {
		
		// 채팅방 목록 조회
		List<ChattingRoom> roomList = service.selectRoomList(loginMember.getMemberNo());
		
		model.addAttribute("roomList", roomList);
		
		return "chatting/chatting";
	}
	
	// 채팅 입장(없으면 생성)
	@GetMapping("/chatting/enter")
	@ResponseBody
	public int chattingEnter(int targetNo, @SessionAttribute("loginMember") Member loginMember
			/*임시 - Member DTO 변경하기*/) {
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("targetNo", targetNo);
		map.put("loginMemberNo", loginMember.getMemberNo());
		
		return service.checkChattingNo(map);
	}
	
	// 채팅방 목록 조회
	@GetMapping(value="/chatting/roomList", produces="application/json; charset=UTF-8")
	@ResponseBody
	public List<ChattingRoom> selectRoomList(@SessionAttribute("loginMember") Member loginMember
			/*임시 - Member DTO 변경하기*/){
		return service.selectRoomList(loginMember.getMemberNo());
	}
	
	// 채팅 상대 검색
	
	// 채팅방 읽음 표시
	
	
	// 채팅방 메세지 목록 조회
	
	
	
	
	
}
