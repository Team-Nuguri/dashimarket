package edu.og.project.goods.model.service;

import java.util.Map;

public interface GoodsService {

	/** 굿즈 상품 목록 조회
	 * @param boardType
	 * @param cp
	 * @return map
	 */
	Map<String, Object> selectGoodsList(String boardType, int cp);

}
