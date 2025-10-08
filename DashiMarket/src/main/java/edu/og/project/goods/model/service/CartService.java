package edu.og.project.goods.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.goods.model.dto.Goods;

public interface CartService {
	
	
	// 
	/** 장바구니 삽입
	 * @param paramMap
	 * @return 행 개수
	 */
	int cartInsert(Map<String, Object> paramMap);
	
	
	// 장바구니 굿즈 목록 조회
	/** 장바구니 굿즈 목록 조회
	 * @param i
	 * @return 굿즈 목록 
	 */
	List<Goods> selectCartGoodsList(int i);


	/** 장바구니 아이템 삭제
	 * @param paramMap
	 * @return 행 개수
	 */
	int cartItemDelete(Map<String, Object> paramMap);

}
