package edu.og.project.goods.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import edu.og.project.common.dto.Review;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.goods.model.dto.GoodsWrite;
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
	 * @param sort 
	 * @return List
	 */
	Map<String, Object> selectReviewList(String boardNo, int cp, String sort);

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
	
	
	
	/** 굿즈 상품 삭제
	 * @param boardNo
	 * @return int 행 개수
	 */
	int goodsDelete(String boardNo);
	
	
	/** 굿즈 상품 등록
	 * @param goodsWrite
	 * @return 게시글 번호
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	String goodsInsert(GoodsWrite goodsWrite) throws IllegalStateException, IOException;
	
	
	
	/** 굿즈 상품 수정
	 * @param goods
	 * @return 행 개수
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	int goodsUpdate(GoodsWrite goods) throws IllegalStateException, IOException;

	/** 굿즈 페이지에서 헤더 검색
	 * @param query
	 * @return list
	 */
	List<Goods> goodsSearch(String query);

}
