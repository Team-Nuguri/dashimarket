package edu.og.project.goods.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.og.project.goods.model.dto.Goods;

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

}
