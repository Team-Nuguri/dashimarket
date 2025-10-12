package edu.og.project.sse.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import edu.og.project.common.dto.Member;
import edu.og.project.sse.dto.Notification;
import edu.og.project.sse.model.service.SseService;

@RestController
public class SseController {
	
	@Autowired
	private SseService service;
	
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	
	// 클라이언트 연결 요청 처리
	@GetMapping("/sse/connect")
	public SseEmitter sseConnect(@SessionAttribute("loginMember") Member loginMember) {
		
		String clientId = loginMember.getMemberNo() + "";
		
		// SseEmitter 객체 생성 -> 연결 대기 시간 10분(ms 단위)
		SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);
		
		emitters.put(clientId, emitter);
		
		// 클라이언트 연결 종료 시 제거
		emitter.onCompletion(() -> emitters.remove(clientId));
		
		// 클라이언트 타임 아웃 시 제거
		emitter.onTimeout(() -> emitters.remove(clientId));
		
		return emitter;
	}
	
	// 알림 메세지 전송
	@PostMapping("/sse/send")
	public void sendNotification(@SessionAttribute("loginMember") Member loginMember
			, @RequestBody Notification notification) {
		
		// 알림 보낸 회원 추가
		notification.setSendMemberNo(loginMember.getMemberNo());
		
		// 알림 받아야하는 회원 번호 + 안읽은 알림 개수 조회
		Map<String, Object> map = service.insertNotification(notification);
		
		// 알림 받을 클라이언트 id
		String clientId = map.get("receiveMemberNo").toString();
		
		// 연결된 emitters에서 clientId가 일치하는 클라이언트 찾기
		SseEmitter emitter = emitters.get(clientId);
		
		// clientId가 일치하는 경우
		if(emitter != null) {
			try {
				emitter.send(map);
				
			}catch(Exception e) {
				emitters.remove(clientId);
			}
		}
	}
	
	// 로그인한 회원의 알림 목록 조회
	@GetMapping("/notification")
	public List<Notification> selectNotificationList(@SessionAttribute("loginMember") Member loginMember){
		
		int memberNo = loginMember.getMemberNo();
		
		return service.selectNotificationList(memberNo);
	}
	
	// 로그인한 회원이 받은 안읽은 알림 개수 조회 
	@GetMapping("/notification/notReadCheck")
	public int notReadCheck(@SessionAttribute("loginMember") Member loginMember) {
		
		int memberNo = loginMember.getMemberNo();
		
		return service.notReadCheck(memberNo);
	}
	
	
	// 알림 삭제
	@DeleteMapping("/notification")
	public void delectNotification(@RequestBody int notificationNo) {
		service.delectNotification(notificationNo);
	}
	
	// 알림 읽음 여부 변경
	@PutMapping("/notification")
	public void updateNotification(@RequestBody int notification) {
		service.updateNotification(notification);
	}
	

}
