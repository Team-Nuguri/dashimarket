package edu.og.project.chatting.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.chatting.model.dto.ChattingRoom;
import edu.og.project.common.dto.Member;
import edu.og.project.admin.model.dto.Report;
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


	/** 채팅 상대 검색(조회)
	 * @param map
	 * @return memberList
	 */
	List<Member> selectTarget(Map<String, Object> map);


	/** 채팅 읽음 표시(업데이트)
	 * @param paramMap
	 * @return int result
	 */
	int updateReadFlag(Map<String, Object> paramMap);


	/** 채팅 메세지 내용 목록 조회
	 * @param paramMap
	 * @return messageList
	 */
	List<ChattingMessage> selectMessageList(Map<String, Object> paramMap);


	/** 메세지 삽입
	 * @param msg
	 * @return result
	 */
	int insertMessage(ChattingMessage msg);


	/** 중고 상품으로 채팅방 입장
	 * @param chat
	 * @return chattingNo(중고상품 번호)
	 */
	int enterJoonggoChat(ChattingRoom chat);


	/** 그냥 나가기 
	 * @param chattingNo
	 * @param memberNo
	 * @return true/false
	 */
	boolean exitChatRoom(Map<String, Object> map);


	/** 신고 후 나가기
	 * @param request
	 * @param reporterNo
	 * @return true/false
	 */
	boolean reportAndExit(Map<String, Integer> map);



}
