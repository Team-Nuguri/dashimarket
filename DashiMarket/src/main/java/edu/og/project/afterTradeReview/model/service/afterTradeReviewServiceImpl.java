package edu.og.project.afterTradeReview.model.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.og.project.afterTradeReview.model.dao.afterTradeReviewMapper;
import edu.og.project.afterTradeReview.model.dto.ReviewWrite;

public class afterTradeReviewServiceImpl implements afterTradeReviewService {
	
	@Autowired
	private afterTradeReviewMapper mapper;

    // 중고상품, 굿즈 후기/별점 등록
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String reviewInsert(ReviewWrite reviewWrite) throws IllegalStateException, IOException {
		
		// xss 방지 처리 (추후 추가 요망)
		
		// 중고상품, 굿즈 후기 먼저 insert
		int result = mapper.reviewInsert(reviewWrite);

		if(result == 0) {
			return null;
		}
		
		// 중고상품, 굿즈 후기 별점 insert
		result = mapper.reviewRatingInsert(reviewWrite);
		
		if(result == 0) {
			return null;
		}
		
		return null;
	}


}
