package edu.og.project.chatting.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.chatting.model.dto.ChattingRoom;
import edu.og.project.chatting.model.dto.ChattingMessage;

public interface ChattingService {

	/** 채팅방 입장
	 * @param map
	 * @return chattingNo
	 */
	int checkChattingNo(Map<String, Integer> map);

	
	/** 채팅방 목록 조회
	 * @param memberNo
	 * @return roomList
	 */
	List<ChattingRoom> selectRoomList(int memberNo);

}
