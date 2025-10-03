package edu.og.project.joonggo.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.joonggo.model.dao.JoonggoMapper;
import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.joonggo.model.dto.SimilarItem;

@Service
public class JoonggoServiceImpl implements JoonggoService {
	
	@Autowired
	JoonggoMapper mapper;
	
	
	
	// 중고상품 상세 조회
	@Override
	public Joonggo selectJoonggoDetail(String joonggoNo) {
		
		
		return mapper.selectJoonggoDetail(joonggoNo);
	}



	// 비슷한 상품 목록 조회
	@Override
	public List<SimilarItem> selectJonggoList(String categoryId) {
		return mapper.selectJonggoList(categoryId);
	}

}
