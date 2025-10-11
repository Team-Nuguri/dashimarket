package edu.og.project.chatting.model.websocket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import edu.og.project.chatting.model.dto.ChattingImg;
import edu.og.project.chatting.model.dto.ChattingMessage;
import edu.og.project.chatting.model.service.ChattingService;
import edu.og.project.common.dto.Member;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component 
public class ChattingWebsocketHandler extends TextWebSocketHandler{
	
	@Autowired
	private ChattingService service;
	
	@Value("${file.upload.path.chat}")
    private String file_upload_path;
	
	// 클라이언트의 최초 웹소켓 요청 시 생성
	private final Map<Integer, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	// 클라이언트와 연결이 완료되고 통신할 준비가 되면 실행
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		// WebSocketSession 객체가 전달되어 필드에 선언해둔 roomSessions에 저장/추가
		 try {
	            // 방 번호 추출
	            Integer chatRoomNo = (Integer) session.getAttributes().get("chattingNo");
	            if (chatRoomNo == null) chatRoomNo = 0;

	            // 방별 세션 set 생성
	            roomSessions.putIfAbsent(chatRoomNo, Collections.synchronizedSet(new HashSet<>()));

	            // 세션 추가
	            roomSessions.get(chatRoomNo).add(session);

	            log.info("[방 {}] 새 연결: {}", chatRoomNo, session.getId());
	      } catch (Exception e) {
	            log.error("afterConnectionEstablished 오류", e);
	      }
	}

	// 클라이언트로 부터 텍스트 메세지를 받았을 때 실행
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {	
		
		// 전달 받은 내용은 JSON 형태의 문자열
		log.info("전달 받은 내용 : {}", message.getPayload());
		
		ChattingMessage msg = objectMapper.readValue(message.getPayload(), ChattingMessage.class);
		// Message 객체 확인
		log.info("Message : {}", msg);
       
		int chatRoomNo = msg.getChattingNo();

        int result = service.insertMessage(msg);
        if (result > 0) {
            msg.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")));
            String json = new Gson().toJson(msg);

            // 같은 방 세션에게만 전송
            sendToRoom(chatRoomNo, json);
        }
	}
	
	// 이미지 파일 전송 -> 문자 인코딩X / 파일 자체를 전송
	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
		try {
            byte[] payload = message.getPayload().array();

            File dir = new File(file_upload_path);
            if (!dir.exists()) dir.mkdirs();

            String originerName = "upload.png";
            String reName = UUID.randomUUID() + ".png";
            Path filePath = Paths.get(dir.getAbsolutePath(), reName);
            Files.write(filePath, payload);

            String imageUrl = "/chat/image/" + reName;

            int chatRoomNo = (Integer) session.getAttributes().get("chattingNo");
            int senderNo = (Integer) session.getAttributes().get("loginMemberNo");

            if (chatRoomNo == 0 || senderNo == 0) {
                log.warn("세션 정보(chatRoomNo/loginMemberNo) 없음");
                return;
            }

            ChattingImg chatImg = ChattingImg.builder()
                    .chattingNo(chatRoomNo)
                    .senderNo(senderNo)
                    .chatImagePath(imageUrl)
                    .originerName(originerName)
                    .reName(reName)
                    .build();

            service.insertChattingImg(chatImg);

            Map<String, Object> msgMap = new HashMap<>();
            msgMap.put("type", "image");
            msgMap.put("url", imageUrl);
            msgMap.put("senderNo", senderNo);
            msgMap.put("chatRoomNo", chatRoomNo);

            String json = new Gson().toJson(msgMap);

            // 같은 방 세션에게만 전송
            sendToRoom(chatRoomNo, json);

            log.info("[방 {}] 이미지 전송 완료: {}", chatRoomNo, imageUrl);

        } catch (Exception e) {
            log.error("handleBinaryMessage 오류", e);
        }
	}

	// 클라이언트와 연결이 종료되면 실행
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		try {
            Integer chatRoomNo = (Integer) session.getAttributes().get("chattingNo");
            if (chatRoomNo != null && roomSessions.containsKey(chatRoomNo)) {
                roomSessions.get(chatRoomNo).remove(session);
                if (roomSessions.get(chatRoomNo).isEmpty()) {
                    roomSessions.remove(chatRoomNo);
                }
            }
            log.info("[방 {}] 연결 종료: {}", chatRoomNo, session.getId());
        } catch (Exception e) {
            log.error("afterConnectionClosed 오류", e);
        }
    }

    // 방 단위 전송 메서드
    private void sendToRoom(int chatRoomNo, String json) {
        Set<WebSocketSession> sessions = roomSessions.getOrDefault(chatRoomNo, Collections.emptySet());
        
        for (Iterator<WebSocketSession> it = sessions.iterator(); it.hasNext();) {
            WebSocketSession s = it.next();
            try {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(json));
                } else {
                    it.remove();
                }
            } catch (IOException e) {
                log.error("메시지 전송 실패", e);
            }
        }
	}
    
    
	
}
