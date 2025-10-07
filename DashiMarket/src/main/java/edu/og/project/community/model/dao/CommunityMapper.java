package edu.og.project.community.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.og.project.common.dto.Comment;
import edu.og.project.community.model.dto.Community;

@Mapper
public interface CommunityMapper {
	
	// 특정 게시판, 특정 카테고리의 삭제되지 않은 게시글 수 조회
	public int getListCount(Map<String, Object> countParam);
	
	// 커뮤니티 목록 조회
	public List<Community> selectCommunityList(Map<String, Object> param, RowBounds rowBounds);

	// 커뮤니티 상세조회
	public Community communityDetail(Map<String, Object> map);

	// 커뮤니티 댓글 조회(비동기)
	public List<Comment> selectCommentList(Map<String, Object> map);


}
