package edu.og.project.chatting.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChattingRoom {

	private int chattingNo;
	private String lastMessage;
	private String sendTime;
	private int targetNo;
	private String targetNickname;
	private String targetProfile;
	private int notReadCount;
	
	// 중고 상품번호로 채팅 구분 입장
	private int sellerNo;
    private int buyerNo;
    private String productNo; // 중고 상품 번호
	
}
