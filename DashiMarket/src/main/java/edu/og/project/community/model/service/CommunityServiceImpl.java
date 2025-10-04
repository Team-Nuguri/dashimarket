package edu.og.project.community.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.common.dto.Pagination;
import edu.og.project.community.model.dao.CommunityMapper;
import edu.og.project.community.model.dto.Community;
import edu.og.project.goods.model.dao.GoodsMapper;

@Service
public class CommunityServiceImpl implements CommunityService {
	
	@Autowired
	private CommunityMapper mapper;
	
	@Autowired
	private GoodsMapper goodsMapper;

	// 커뮤니티 목록 조회
	@Override
	public Map<String, Object> selectCommunityList(String boardType, int cp) {
		// 특정 게시판의 삭제되지 않은 게시글 수 조회
		int listCount = goodsMapper.getListCount(boardType);
		
		Pagination pagination = new Pagination(cp, listCount);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		
		List<Community> boardList = mapper.selectCommunityList(boardType, rowBounds);
		
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

}
