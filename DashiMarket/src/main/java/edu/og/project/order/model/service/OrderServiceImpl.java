package edu.og.project.order.model.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.project.goods.model.dto.Goods;
import edu.og.project.order.model.dao.OrderMapper;
import edu.og.project.order.model.dto.OrderDto;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	OrderMapper mapper;

	// 주문 상품 insert
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> insertOrder(Map<String, Object> paramMap) {
		
		// 주문 번호 생성 & 삽입
		Map<String, Object> map = new HashMap<>();
		
		int result =  mapper.insertOrder(paramMap);
		
		
		if(result != 0) {
			long orderNo = (long) paramMap.get("orderNo");
			// 주문 상품 삽입
			paramMap.put("orderNo", orderNo);
			
			List<Map<String, Object>> itemList = (List<Map<String, Object>>) paramMap.get("orderItems");

			for (Map<String, Object> mapItem : itemList) {
			    Goods item = new Goods();
			    item.setBoardNo((String) mapItem.get("boardNo"));
			    item.setQuantity((Integer) mapItem.get("quantity"));
			    
			    Map<String, Object> itemParam = new HashMap<>();
			    itemParam.put("orderNo", orderNo);
			    itemParam.put("boardNo", item.getBoardNo());
			    itemParam.put("quantity", item.getQuantity());
			    
			    int insertResult = mapper.orderItemInsert(itemParam);
			    if (insertResult == 0) {
			        throw new RuntimeException("주문 상품 등록에 실패");
			    }
			}
			
			// 배송 정보
			result = mapper.shippingInsert(paramMap);
			
			if(result == 0) {
				throw new RuntimeException("배송정보 등록에 실패");
			}
			
			map.put("orderNo", orderNo);
			
		} else {
			map.put("orderNo", 0);
			return map;
		}
		
		
		
		return map;
	}
	
	
	// 실패 시 주문 삭제
	@Override
	public void orderDelete(long parseLong) {
		// 상품 먼저 삭제
		mapper.orderItemDelete(parseLong);
		// 그거 삭제
		mapper.shippingDelete(parseLong);
		
		// 주문 정보 삭제
		mapper.orderDelete(parseLong);
		
	}

	
	// 주문 검증 ?
	@Override
	public int paymentComplete(Map<String, Object> paramMap) {
		
		int result = mapper.selectTotalPrice(paramMap);
		
		if(result == 0) {
			return result;
		}
		
		// 장바구니 해당 상품 삭제
		int cartResult = mapper.deleteCartItem(paramMap);
		
		
		result = mapper.insertPayment(paramMap);
		
		
		return result;
	}

	
	
	// 주문 완료
	@Override
	public OrderDto selectOrderCompleteList(String orderNo) {
		return mapper.selectOrderCompleteList(orderNo);
	}

}
