package edu.og.project.chatting.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.chatting.model.dao.ChattingMapper;
import edu.og.project.chatting.model.dto.ChattingRoom;
import edu.og.project.chatting.model.dto.ChattingImg;
import edu.og.project.chatting.model.dto.ChattingMessage;
import edu.og.project.common.dto.Member;
import edu.og.project.common.utility.Util;

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

	// 채팅 이미지
	@Override
	public void insertChattingImg(ChattingImg chatImg) {
		mapper.insertChattingImg(chatImg);
	}

	// 중고상품번호로 채팅방 구분
	@Override
	public int checkJoonggoChat(Map<String, Object> map) {
		
		Integer jchattingNo = mapper.checkJoonggoChat(map);
		
		if(jchattingNo != null) {
			return jchattingNo;
		
		}else {
	        // 채팅방이 없으면 새로 생성
	        // 2. createJoonggoRoom은 성공한 행의 수(1)를 반환
	        int result = mapper.createJoonggoRoom(map);
	        
	        if (result > 0) {
	            // 3. 삽입 성공 시, selectKey로 map에 담긴 chattingNo를 가져옴
	            jchattingNo = (Integer) map.get("chattingNo");
	            // 이 시점에서 jchattingNo가 null이 아닌 유효한 Integer 값으로 설정됩니다.
	        } else {
	            // 4. 삽입 실패 시 (DB 오류 등), jchattingNo를 null로 설정하거나 예외 처리
	            // 이 로직이 없으면 'jchattingNo'가 이전에 선언된 초기 null 상태로 남을 수 있어 경고가 발생했을 수 있습니다.
	            // 명시적으로 null 처리하여 안정성을 높입니다.
	            jchattingNo = null; 
	        }
		}
		
		return jchattingNo;
	}
	

	

		

}
