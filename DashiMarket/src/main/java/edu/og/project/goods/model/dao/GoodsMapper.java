package edu.og.project.goods.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper {

	// 게시글 수 조회
	public int getListCount(String boardType);

}
