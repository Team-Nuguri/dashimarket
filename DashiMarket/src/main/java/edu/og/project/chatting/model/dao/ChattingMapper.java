package edu.og.project.chatting.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.chatting.model.dto.ChattingRoom;
import edu.og.project.chatting.model.dto.ChattingMessage;

@Mapper
public interface ChattingMapper {

	// 채팅방 입장
	public int checkChattingNo(Map<String, Integer> map);

	// 채팅방 생성
	public int createChattingRoom(Map<String, Integer> map);

	// 채팅방 목록 조회
	public List<ChattingRoom> selectRoomList(int memberNo);


}
