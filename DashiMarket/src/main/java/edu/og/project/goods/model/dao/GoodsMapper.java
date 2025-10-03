package edu.og.project.goods.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper {

	public int getListCount(String boardType);

}
