package edu.og.project.joonggo.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.joonggo.model.dto.SimilarItem;

public interface JoonggoService {
	
	
	
	/** 중고 상세 조회
	 * @param map
	 * @return Joonggo
	 */
	Joonggo selectJoonggoDetail(String joonggoNo);

	
	
	/** 비슷한 상품 목록 조회
	 * @param categoryId
	 * @return List
	 */
	List<SimilarItem> selectJonggoList(String categoryId);

}
