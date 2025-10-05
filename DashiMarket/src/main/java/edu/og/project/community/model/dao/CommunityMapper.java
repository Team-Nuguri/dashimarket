package edu.og.project.community.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.og.project.community.model.dto.Community;

@Mapper
public interface CommunityMapper {

	// 커뮤니티 목록 조회
	public List<Community> selectCommunityList(Map<String, Object> param, RowBounds rowBounds);

}
