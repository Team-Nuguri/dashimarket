package edu.og.project.goods.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Image;
import edu.og.project.common.dto.Review;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.goods.model.dto.GoodsWrite;
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
	public List<Review> selectReviewList(Map<String, Object> paramMap, RowBounds rowBounds);
	
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
	
	
	// 굿즈 보드 insert
	public int goodsBoardInsert(GoodsWrite goodsWrite);

	// 이미지 삽입
	public int imageInsert(Image image);
	
	
	// 굿즈 가격, 재고 insert
	public int goodsInsert(GoodsWrite goodsWrite);
	
	// 보드 업데이트
	public int goodsBoardUpdate(GoodsWrite goods);
	
	// 굿즈 수량 재고
	public int goodsUpdate(GoodsWrite goods);
	
	// 굿즈 이미지 수정
	public int goodsImageUpdate(Image image);
	
	
	
	// 장바구니 굿즈 목록 조회
	public List<Goods> selectCartGoodsList(int i);

}
