package edu.og.project.chatting.model.websocket;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import edu.og.project.chatting.model.dto.ChattingMessage;
import edu.og.project.chatting.model.service.ChattingService;
import edu.og.project.temp.model.Member;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component // bean 등록
public class ChattingWebsocketHandler extends TextWebSocketHandler{
	
	@Autowired
	private ChattingService service;
	
	// 클라이언트의 최초 웹소켓 요청 시 생성
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

	// 클라이언트와 연결이 완료되고 통신할 준비가 되면 실행
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		// WebSocketSession 객체가 전달되어 필드에 선언해둔 sessions에 저장/추가
		sessions.add(session);
		
		log.info("{}연결됨", session.getId());
	}

	// 클라이언트로 부터 텍스트 메세지를 받았을 때 실행
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {	
		
		// 전달 받은 내용은 JSON 형태의 문자열
		log.info("전달 받은 내용 : {}", message.getPayload());
		
		// ObjectMapper : Jackson에서 제공하는 객체 (JSON String -> DTO Object)
		ObjectMapper objectMapper = new ObjectMapper();
		
		ChattingMessage msg = objectMapper.readValue(message.getPayload(), ChattingMessage.class);
		
		// Message 객체 확인
		log.info("Message : {}", msg);
		
		// DB에 메세지 삽입 서비스 호출
		int result = service.insertMessage(msg);
		
		if(result > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.DD");
			msg.setSendTime(sdf.format(new Date()));
			
			for(WebSocketSession s : sessions) {
				
				// session 꺼내기
				HttpSession temp = (HttpSession)s.getAttributes().get("session");
				
				// 로그인한 회원의 번호 얻기
				int loginMemberNo = ((Member)temp.getAttribute("loginMember")).getMemberNo();
				
				// 로그인 상태인 회원 중 targetNo 또는 sendMember가 일치하는 회원에게 메세지 전달
				if(loginMemberNo == msg.getTargetNo() || loginMemberNo == msg.getSendMember()) {
					s.sendMessage(new TextMessage(new Gson().toJson(msg)));
				}
			}
		}
	}

	// 클라이언트와 연결이 종료되면 실행
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// sessions에서 나간 클라이언트의 정보 제거
		sessions.remove(session);
	}
	
}
