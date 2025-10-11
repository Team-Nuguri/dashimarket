package edu.og.project.chatting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattingImg {
	
	private int chatImageNo;
	private String chatImagePath;
	private String originerName;
	private String reName;
	private int chattingNo;
	private int senderNo;
}
