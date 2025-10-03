package edu.og.project.joonggo.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.joonggo.model.dto.SimilarItem;

@Mapper
public interface JoonggoMapper {
	
	
	// 중고 상품 상세 조회
	Joonggo selectJoonggoDetail(String joonggoNo);
	
	
	// 비슷한 상품 목록 조회
	List<SimilarItem> selectJonggoList(Map<String, Object> map);

	
	// 중고 상품 삭제
	int deleteJoonggoItem(String joonggoNo);
	
	

}
