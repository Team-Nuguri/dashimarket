package edu.og.project.chatting.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.chatting.model.dto.ChattingRoom;
import edu.og.project.common.dto.Member;
import edu.og.project.chatting.model.dto.ChattingMessage;

@Mapper
public interface ChattingMapper {

	// 채팅방 입장
	public int checkChattingNo(Map<String, Integer> map);

	// 채팅방 생성
	public int createChattingRoom(Map<String, Integer> map);

	// 채팅방 목록 조회
	public List<ChattingRoom> selectRoomList(int memberNo);

	// 채팅 상대 검색(조회)
	public List<Member> selectTarget(Map<String, Object> map);

	// 채팅 읽음 표시
	public int updateReadFlag(Map<String, Object> paramMap);

	// 채팅 메세지 내용 목록 조회
	public List<ChattingMessage> selectMessageList(int chattingNo);
	
	// 메세지 삽입
	public int insertMessage(ChattingMessage msg);


}
