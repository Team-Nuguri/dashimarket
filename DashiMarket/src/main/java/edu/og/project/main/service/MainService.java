package edu.og.project.main.service;

import java.util.List;
import java.util.Map;

public interface MainService {

	
	/** 인트로 검색
	 * @param query
	 * @return list
	 */
	List<Map<String, Object>> introSearch(String query);

}
