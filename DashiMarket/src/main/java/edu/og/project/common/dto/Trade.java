package edu.og.project.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
	
	private int tradeNo;
	private int sellerNo;
	private int buyerNo;
	private String tradeDate;
	private String reviewFl;
	private String boardNo; // 중고 상품 번호

}
