package edu.og.project.goods.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.goods.model.dto.Goods;

public interface GoodsService {

	/** 굿즈 상품 목록 조회
	 * @param boardType
	 * @param cp
	 * @return map
	 */
	Map<String, Object> selectGoodsList(String boardType, int cp);

	/** 굿즈 정렬 목록 조회
	 * @param boardType
	 * @param cp
	 * @param sortType
	 * @return goodsList
	 */
	Map<String, Object> sortGoodsList(String boardType, int cp, String sortType);

}
