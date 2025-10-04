package edu.og.project.community.model.service;

import java.util.Map;

public interface CommunityService {

	/** 커뮤니티 목록 조회
	 * @param boardType
	 * @param cp
	 * @return map
	 */
	Map<String, Object> selectCommunityList(String boardType, int cp);

	/** 커뮤니티 정렬
	 * @param boardType
	 * @param cp
	 * @param sortType
	 * @return map
	 */
	Map<String, Object> sortCommunityList(String boardType, int cp, String sortType);

}
