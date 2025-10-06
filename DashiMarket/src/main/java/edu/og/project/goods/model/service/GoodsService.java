package edu.og.project.goods.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.common.dto.Review;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.goods.model.dto.OtherGoods;

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
	
	
	
	/** 굿즈 상세 조회
	 * @param boardNo
	 * @return Goods
	 */
	Goods selectGoodsDetail(String boardNo);
	
	
	
	/** 굿즈 리뷰 목록 조회
	 * @param boardNo
	 * @param cp 
	 * @return List
	 */
	Map<String, Object> selectReviewList(String boardNo, int cp);

	/** 굿즈 qna 목록 조회
	 * @param boardNo
	 * @param cp
	 * @return map
	 */
	Map<String, Object> selectQnaList(String boardNo, int cp);
	
	
	
	/** 상세 페이지 내 다른 굿즈 리스트
	 * @param boardNo
	 * @return 굿즈 리스트
	 */
	List<OtherGoods> selectOtherGoodsList(String boardNo);

}
