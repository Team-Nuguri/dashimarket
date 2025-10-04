package edu.og.project.community.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.og.project.community.model.dto.Community;

@Mapper
public interface CommunityMapper {

	// 굿즈 목록 조회
	public List<Community> selectCommunityList(String boardType, RowBounds rowBounds);

}
