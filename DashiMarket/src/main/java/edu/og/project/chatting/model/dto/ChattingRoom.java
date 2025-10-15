package edu.og.project.chatting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChattingRoom {

	private int chattingNo;
	private String lastMessage;
	private String sendTime;
	private int targetNo; 
	private String targetNickname;
	private String targetProfile;
	private int notReadCount;
	
	private String productNo; // 중고 상품 번호
	private int sellerNo;   // 판매자 회원 번호
    private int buyerNo;   // 채팅 요청자 (현재 로그인 회원 번호)
    
    // 채팅 신고
    private int reportedMemberNo;
    private String reportReason;
	
}
