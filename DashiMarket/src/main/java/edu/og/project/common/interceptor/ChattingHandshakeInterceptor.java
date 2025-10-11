package edu.og.project.common.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import edu.og.project.common.dto.Member;
import jakarta.servlet.http.HttpSession;

@Component
public class ChattingHandshakeInterceptor implements HandshakeInterceptor{
	
	// WebSocketHandler가 동작하기 전
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		
		if(request instanceof ServletServerHttpRequest) {
			
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
			
			// 웹소켓 접속한 클라이언트의 세션을 얻어옴
			HttpSession session = servletRequest.getServletRequest().getSession();
			
			// 로그인 회원 정보 저장
	        Member loginMember = (Member) session.getAttribute("loginMember");
	        
	        if (loginMember != null) {
	            attributes.put("loginMemberNo", loginMember.getMemberNo());
	        }
	        
			// 쿼리 파라미터로 전달된 채팅방 번호 읽기
			String query = servletRequest.getServletRequest().getQueryString(); // ex) chattingNo=5
			
			if (query != null && query.startsWith("chattingNo=")) {
				try {
				    int chattingNo = Integer.parseInt(query.replace("chattingNo=", ""));
				attributes.put("chattingNo", chattingNo);
				} catch (NumberFormatException e) {
					System.out.println("채팅방 번호 파싱 실패: " + query);
			    }
			}
			
			attributes.put("session", session);

		}
		
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		
	}
	
	

}
