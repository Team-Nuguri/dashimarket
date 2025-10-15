package edu.og.project.afterTradeReview.model.dao;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.afterTradeReview.model.dto.ReviewWrite;
import edu.og.project.goods.model.dto.GoodsWrite;

@Mapper
public interface afterTradeReviewMapper {
	
	// 중고상품, 굿즈 후기 insert
	public int reviewInsert(ReviewWrite reviewWrite);
	
	// 중고상품, 굿즈 후기 별점 insert
	public int reviewRatingInsert(ReviewWrite reviewWrite);


}
