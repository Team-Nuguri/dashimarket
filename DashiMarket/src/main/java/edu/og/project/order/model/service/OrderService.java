package edu.og.project.order.model.service;

import java.util.Map;

import edu.og.project.order.model.dto.OrderDto;

public interface OrderService {
	
	
	/** 주문 정보 insert
	 * @param paramMap
	 * @return 
	 */
	Map<String, Object> insertOrder(Map<String, Object> paramMap);
	
	
	// 결제 취소 or 실패 시 주문 관련 삭제
	void orderDelete(long parseLong);

	
	// 검증
	int paymentComplete(Map<String, Object> paramMap);

	
	
	/** 주문 완료 정보 가져오기
	 * @param orderNo
	 * @return OrderDto
	 */
	OrderDto selectOrderCompleteList(String orderNo);
	
}
