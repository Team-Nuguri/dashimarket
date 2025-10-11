package edu.og.project.sse.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.sse.dto.Notification;

public interface SseService {

	/** 알림 삽입 후 알림 받을 회원 번호 + 안읽은 알림 개수 반환
	 * @param notification
	 * @return
	 */
	Map<String, Object> insertNotification(Notification notification);

	
	/** 로그인한 회원의 알림 목록 조회
	 * @param memberNo
	 * @return selectList
	 */
	List<Notification> selectNotificationList(int memberNo);
	
	/** 읽지 않은 알림 개수 조회
	 * @param memberNo
	 * @return count
	 */
	int notReadCheck(int memberNo);
	
	/** 알림 삭제
	 * @param notificationNo
	 */
	void delectNotification(int notificationNo);



}
