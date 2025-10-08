package edu.og.project.chatting.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.chatting.model.dao.ChattingMapper;
import edu.og.project.chatting.model.dto.ChattingRoom;
import edu.og.project.chatting.model.dto.ChattingMessage;
import edu.og.project.common.utility.Util;
import edu.og.project.temp.model.Member;

@Service
public class ChattingServiceImpl implements ChattingService{
	
	@Autowired
	private ChattingMapper mapper;

	// 채팅방 입장(없으면 생성
	@Override
	public int checkChattingNo(Map<String, Integer> map) {
		
		// 채팅방 번호 조회
		int chattingNo = mapper.checkChattingNo(map);
		
		// 기존에 채팅방이 없을 경우
		if(chattingNo == 0) {
			// 채팅방 생성
			chattingNo = mapper.createChattingRoom(map);
			
			if(chattingNo > 0) chattingNo = map.get("chattingNo");
		}
		
		return chattingNo;
	}

	// 채팅방 목록 조회
	@Override
	public List<ChattingRoom> selectRoomList(int memberNo) {
		return mapper.selectRoomList(memberNo);
	}

	// 채팅 상대 검색(조회)
	@Override
	public List<Member> selectTarget(Map<String, Object> map) {
		return mapper.selectTarget(map);
	}

	// 채팅 읽음 표시(업데이트)
	@Override
	public int updateReadFlag(Map<String, Object> paramMap) {
		return mapper.updateReadFlag(paramMap);
	}

	// 채팅 메세지 내용 목록 조회
	@Override
	public List<ChattingMessage> selectMessageList(Map<String, Object> paramMap) {
		
		List<ChattingMessage> messageList = mapper.selectMessageList(Integer.parseInt(String.valueOf(paramMap.get("chattingNo"))));
		
		if(!messageList.isEmpty()) mapper.updateReadFlag(paramMap);
		
		return messageList;
	}

	// 메세지 삽입
	@Override
	public int insertMessage(ChattingMessage msg) {
		msg.setMessageContent(Util.XSSHandling(msg.getMessageContent()));
		return mapper.insertMessage(msg);
	}

	

}
