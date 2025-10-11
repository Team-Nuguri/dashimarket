package edu.og.project.sse.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.sse.dto.Notification;

@Mapper
public interface SseMapper {

	// 알림 삽입
	int insertNotification(Notification notification);

	// 알림 받아야하는 회원 번호 + 안읽은 알림 개수 조회
	Map<String, Object> selectReceiveMember(int notificationNo);

	// 로그인한 회원의 알림 목록 조회
	List<Notification> selectNotificationList(int memberNo);

	// 알림 개수 조회
	int notReadCheck(int memberNo);
	
	// 알림 삭제
	void delectNotification(int notificationNo);

	// 알림 읽음 처리
	void updateNotification(int notification);


}
