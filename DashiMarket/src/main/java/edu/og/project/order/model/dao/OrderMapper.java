package edu.og.project.order.model.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.order.model.dto.OrderDto;

@Mapper
public interface OrderMapper {
	
	// 주문 정보 삽입
	int insertOrder(Map<String, Object> paramMap);
	
	
	// 주문 상품 삽입
	int orderItemInsert(Map<String, Object> paramMap);

	
	// 배송 정보 삽입
	int shippingInsert(Map<String, Object> paramMap);

	
	// 삭제 
	void orderDelete(long parseLong);

	// 주문 상품 삭제
	void orderItemDelete(long parseLong);

	// 배송 삭제
	void shippingDelete(long parseLong);
	

	// 결제 금액 주문 금액 일치하는지 확인
	int selectTotalPrice(Object object);

	
	// 결제 테이블 삽입
	int insertPayment(Map<String, Object> paramMap);

	
	// 주문 완료
	OrderDto selectOrderCompleteList(String orderNo);

	
	// 장바구니에서 삭제
	int deleteCartItem(Map<String, Object> paramMap);

}
