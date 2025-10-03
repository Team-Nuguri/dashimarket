package edu.og.project.goods.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.goods.model.dao.GoodsMapper;

@Service
public class GoodsServiceImpl implements GoodsService {
	
	@Autowired
	private GoodsMapper mapper;

	// 굿즈 상품 목록 조회
	@Override
	public Map<String, Object> selectGoodsList(String boardType, int cp) {
		// 삭제되지 않은 게시글 수 조회
		int listCount = mapper.getListCount(boardType);
		return null;
	}

}
