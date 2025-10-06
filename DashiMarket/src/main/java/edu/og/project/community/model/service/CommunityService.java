package edu.og.project.community.model.service;

import java.util.Map;

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
	 * @return
	 */
	Community communityDetail(Map<String, Object> map);

}
