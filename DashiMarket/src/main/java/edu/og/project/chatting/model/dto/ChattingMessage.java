package edu.og.project.chatting.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChattingMessage {

	private int messageNo;
	private String messageContent;
	private String readFlag;
	private int sendMember;
	private int targetNo; 
	private int chattingNo;
	private String sendTime;
	private String sendImg;
	private String sendNickname;
	private String targetNickname;
}
