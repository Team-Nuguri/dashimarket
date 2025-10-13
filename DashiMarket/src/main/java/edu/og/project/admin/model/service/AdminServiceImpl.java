package edu.og.project.admin.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.admin.model.dto.Report;
import edu.og.project.common.dto.Pagination;
import edu.og.project.goods.model.dao.GoodsMapper;
import edu.og.project.goods.model.dto.Goods;

import edu.og.project.admin.model.dao.AdminMapper;
import edu.og.project.common.dto.Member;
import edu.og.project.goods.model.dto.Goods;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
	
	private final AdminMapper mapper;

	// 오늘 가입자 수
	@Override
	public Integer getTodayJoinCount() {
		return mapper.selectTodayJoincount();
	}

	// 총 회원 수
	@Override
	public Integer getTotalUserCount() {
		return mapper.selectTotalUsercount();
	}

	// 전체 회원 조회
	@Override
	public List<Member> selectAllMembers() {
		return mapper.selectAllMembers();
	}

	// 굿즈 상품 조회
	@Override
	public List<Goods> selectProducts(String sort) {
		return mapper.selectProducts(sort);
	}

	// 신고
	@Override
	public Map<String, Object> selectReportList(String keyword, String reportResult, int cp) {
		// 특정 게시판의 삭제되지 않은 게시글 수 조회
		int listCount = mapper.getReportListCount(keyword, reportResult);

		Pagination pagination = new Pagination(cp, listCount, 16);
		int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
		RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());

		List<Report> reportList = mapper.selectReportList(keyword, reportResult, rowBounds);

		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("reportList", reportList);

		return map;
	}

	@Override
	public Report selectReportDetail(int reportNo) {
		
		return mapper.selectReportDetail(reportNo);

	}
	
	@Override
	public int updateReportResult(int reportNo, String resultType) {
		return mapper.updateReportResult(reportNo, resultType);
	}
	
}
