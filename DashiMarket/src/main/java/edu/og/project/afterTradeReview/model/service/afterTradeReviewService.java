package edu.og.project.afterTradeReview.model.service;

import java.io.IOException;
import java.util.Map;

import edu.og.project.afterTradeReview.model.dto.ReviewWrite;
import edu.og.project.goods.model.dto.GoodsWrite;

public interface afterTradeReviewService {
	

	/** 중고상품, 굿즈 후기/별점 등록
	 * @param reviewWrite
	 * @return 게시글 번호
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	// String reviewInsert(ReviewWrite reviewWrite) throws IllegalStateException, IOException;

	int insertReview(ReviewWrite reviewWrite);

	Map<String, Object> getJoonggoReviewInfo(String boardNo, int memberNo);

	Map<String, Object> getGoodsReviewInfo(String boardNo, int memberNo);

	
	
}
