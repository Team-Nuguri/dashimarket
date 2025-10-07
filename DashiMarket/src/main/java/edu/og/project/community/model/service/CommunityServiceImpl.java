package edu.og.project.community.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Pagination;
import edu.og.project.common.utility.Util;
import edu.og.project.community.model.dao.CommunityMapper;
import edu.og.project.community.model.dto.Community;
import edu.og.project.goods.model.dao.GoodsMapper;

@Service
public class CommunityServiceImpl implements CommunityService {
	
	@Autowired
	private CommunityMapper mapper;
	
	// 커뮤니티 목록 조회
	@Override
	public Map<String, Object> selectCommunityList(String boardType, int cp, String category, String sort) {
		// 특정 게시판의 특정 카테고리에서 삭제되지 않은 게시글 수 조회
		Map<String, Object> countParam = new HashMap<>();
		countParam.put("boardType", boardType);
		countParam.put("category", category);
		
		int listCount = mapper.getListCount(countParam);
		
		// 페이지네이션
		Pagination pagination = new Pagination(cp, listCount);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
		
		// 매퍼로 보낼 파라미터
		Map<String, Object> param = new HashMap<>();
		param.put("boardType", boardType);
		param.put("category", category);
		param.put("sort", sort);
		
		List<Community> boardList = mapper.selectCommunityList(param, rowBounds);
		
		// 조회 결과 return
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

	// 커뮤니티 상세조회
	@Override
	public Community communityDetail(Map<String, Object> map) {
		return mapper.communityDetail(map);
	}

	// 댓글 조회
	@Override
	public List<Comment> selectComment(Map<String, Object> map) {
		return mapper.selectCommentList(map);
	}

	// 댓글 등록
	@Override
	public int insertComment(Comment comment) {
		// XSS 방지 처리
		comment.setCommentContent(Util.XSSHandling(comment.getCommentContent()));
		
		int result = mapper.insertComment(comment);
		
		// 댓글 등록 성공시 댓글 번호 반환
		if(result > 0) {
			result = comment.getCommentNo();
			System.out.println(result);
		}
		
		return result;
	}


}
