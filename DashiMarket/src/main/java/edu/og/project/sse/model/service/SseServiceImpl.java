package edu.og.project.sse.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.sse.dto.Notification;
import edu.og.project.sse.model.dao.SseMapper;

@Service
public class SseServiceImpl implements SseService{
	
	@Autowired
	private SseMapper mapper;

	// 알림 삽입 후 알림 받을 회원 번호 반환
	@Override
	public Map<String, Object> insertNotification(Notification notification) {
		
		// 결과 저장용
		Map<String, Object> map = new HashMap<>();
		
		// 알림 삽입
		int result = mapper.insertNotification(notification);
		
		if(result > 0) { // 알림 삽입 성공시
			
			// 알림 받아야하는 회원 번호 + 안읽은 알림 개수 조회
			map = mapper.selectReceiveMember(notification.getNotificationNo());
			
			// 채팅 알림인 경우 채팅방 번호, 알림 번호 추가
			if(notification.getNotificationType().equals("chatting")) {
				String url = notification.getNotificationUrl();
				String[] arr = url.split("chat-no=");
				String chatNo = arr[arr.length-1];
				
				map.put("notificationNo", notification.getNotificationNo()); 
				map.put("chattingRoomNo", chatNo);
			}
		}
		return map;
	}

	// 로그인한 회원의 알림 목록 조회
	@Override
	public List<Notification> selectNotificationList(int memberNo) {
		return mapper.selectNotificationList(memberNo);
	}
	
	// 알림 개수 조회
	@Override
	public int notReadCheck(int memberNo) {
		return mapper.notReadCheck(memberNo);
	}

	// 알림 삭제
	@Override
	public void delectNotification(int notificationNo) {
		mapper.delectNotification(notificationNo);
	}

	// 알림 읽음 처리
	@Override
	public void updateNotification(int notification) {
		mapper.updateNotification(notification);
	}


}
