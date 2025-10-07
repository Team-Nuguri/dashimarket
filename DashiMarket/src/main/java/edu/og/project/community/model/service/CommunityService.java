package edu.og.project.community.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.common.dto.Comment;
import edu.og.project.community.model.dto.Community;

public interface CommunityService {

	/** 커뮤니티 목록 조회
	 * @param boardType
	 * @param cp
	 * @param sortType 
	 * @param category 
	 * @return map
	 */
	Map<String, Object> selectCommunityList(String boardType, int cp, String category, String sort);

	/** 커뮤니티 상세조회
	 * @param map
	 * @return community
	 */
	Community communityDetail(Map<String, Object> map);

	/** 커뮤니티 댓글 조회(비동기)
	 * @param map
	 * @return commentList
	 */
	List<Comment> selectComment(Map<String, Object> map);

	/** 댓글 등록
	 * @param comment
	 * @return result
	 */
	int insertComment(Comment comment);

}
