package edu.og.project.goods.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.common.dto.Pagination;
import edu.og.project.goods.model.dao.GoodsMapper;
import edu.og.project.goods.model.dto.Goods;

@Service
public class GoodsServiceImpl implements GoodsService {
	
	@Autowired
	private GoodsMapper mapper;

	// 굿즈 상품 목록 조회
	@Override
	public Map<String, Object> selectGoodsList(String boardType, int cp) {
		// 특정 게시판의 삭제되지 않은 게시글 수 조회
		int listCount = mapper.getListCount(boardType);
		
		Pagination pagination = new Pagination(cp, listCount);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		
		List<Goods> boardList = mapper.selectGoodsList(boardType, rowBounds);
		
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

	// 굿즈 정렬 목록 조회
	@Override
	public Map<String, Object> sortGoodsList(String boardType, int cp, String sortType) {
		int listCount = mapper.getListCount(boardType);
		
		Pagination pagination = new Pagination(cp, listCount);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		
		// 정렬 타입에 맞게 조회해오기
		List<Goods> sortList = null;
		
		if(sortType != null) {
			
			// 인기순(기본)
			if(sortType.equals("popular")) sortList = mapper.selectGoodsList(boardType, rowBounds);
			
			// 낮은 가격순
			if(sortType.equals("lowPrice")) sortList = mapper.sortLowPrice(boardType, rowBounds);
			
			// 높은 가격순
			if(sortType.equals("highPrice")) sortList = mapper.sortHighPrice(boardType, rowBounds);
		}
			
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", sortList);
		
		return map;
	}

}
