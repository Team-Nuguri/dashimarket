package edu.og.project.goods.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Review;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.goods.model.dto.OtherGoods;

@Mapper
public interface GoodsMapper {

	// 특정 게시판의 게시글 수 조회
	public int getListCount(String boardType);

	// 특정 게시판에서 현재 페이지에 해당하는 부분에 대한 게시글 목록 조회
	public List<Goods> selectGoodsList(String boardType, RowBounds rowBounds);

	// 낮은 가격순 조회
	public List<Goods> sortLowPrice(String boardType, RowBounds rowBounds);

	// 높은 가격순 조회
	public List<Goods> sortHighPrice(String boardType, RowBounds rowBounds);
	
	
	// 굿즈 상세 조회
	public Goods selectGoodsDetail(String boardNo);
	
	
	// 굿즈 리뷰 목록 조회
	public List<Review> selectReviewList(String boardNo, RowBounds rowBounds);
	
	// 특정 게시판 리뷰 수 조회
	public int getReviewListCount(String boardNo);
	
	
	// 특정 게시글  qna 수 조회
	public int getQnaListCount(String boardNo);
	
	// 특정 게시글 qna 목록 조회
	public List<Comment> selectCommentList(String boardNo, RowBounds rowBounds);
	
	
	// 다른 굿즈 목록 조회
	public List<OtherGoods> selectOtherGoodsList(String boardNo);
	
	// 굿즈 삭제
	public int goodsDelete(String boardNo);

}
