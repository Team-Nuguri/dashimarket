package edu.og.project.order.model.dto;

import java.util.List;

import edu.og.project.goods.model.dto.Goods;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDto {
	
	// 상품 목록
	private List<Goods> orderItems;
	// 총 가격
	private int totalPrice;
	
	
	// 주문 정보
	private long orderNo;
	private String memberName; //세션에서 값 세팅
	private String recipientName; // 수령자 이름
	private String recipientTel; //수령자 번호
	private String address; // 배송 주소
	
	
	// 결제 정보
	private int payPrice; // 결제 금액
	private String payMethod; // 결제 수단
	private String payDate; // 결제일
	

}
