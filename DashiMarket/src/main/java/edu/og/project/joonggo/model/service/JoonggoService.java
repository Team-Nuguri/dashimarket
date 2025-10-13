package edu.og.project.joonggo.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.joonggo.model.dto.JoonggoWrite;
import edu.og.project.joonggo.model.dto.SimilarItem;

public interface JoonggoService {
	
	
	/** 중고 상품 목록 조회 (KJK)
	 * @param boardType
	 * @param cp
	 * @return map
	 */
	Map<String, Object> selectJoonggoList(String boardType, int cp);
	
	
	/** 중고상품 목록 정렬  (KJK)
	 * @param boardType
	 * @param cp
	 * @param sortType
	 * @return map
	 */
	Map<String, Object> sortJoonggoList(String boardType, int cp, String sortType);

	
	
	/** 중고 상세 조회
	 * @param map
	 * @return Joonggo
	 */
	Joonggo selectJoonggoDetail(String joonggoNo);

	
	
	/** 비슷한 상품 목록 조회
	 * @param map
	 * @return List
	 */
	List<SimilarItem> selectJonggoList(Map<String, Object> map);


	
	
	/** 중고 상품 삭제
	 * @param String
	 * @return int 행 개수
	 */
	int deleteJoonggoItem(String joonggoNo);



	/** 중고 상품 삽입
	 * @param joonggoWrite
	 * @return 행 개수
	 */
	String joonggoInsert(JoonggoWrite joonggoWrite)  throws IllegalStateException, IOException;



	/** 중고 상품 수정
	 * @param map
	 * @return 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	String joonggoUpdate(Map<String, Object> map) throws IllegalStateException, IOException;


	
	// 좋아요 삽입 or 삭제
	int joonggoLike(Map<String, Object> paramMap);


	
	// 현재 로그인 중인 유저가 좋아요 눌렀는지 확인
	int likeSelect(Map<String, Object> map);



	
	/** 조회수 증가
	 * @param joonggoNo
	 * @return int 행 개수
	 */
	int updateReadCount(String joonggoNo);


}
