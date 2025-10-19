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
import edu.og.project.order.model.dto.OrderDto;
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
	
	// 오늘 신고된 수
	@Override
	public Integer getTodayReportCount() {
		return mapper.getTodayReportCount();
	}
	
	// 배송전
	@Override
	public Integer getBeforeDelivery() {
		return mapper.getBeforeDelivery();
	}

	// 배송중
	@Override
	public Integer getInDelivery() {
		return mapper.getInDelivery();
	}

	// 배송완료
	@Override
	public Integer getDeliveryCompleted() {
		return mapper.getDeliveryCompleted();
	}

	// 전체 회원 조회
	@Override
	public List<Member> selectAllMembers() {
		return mapper.selectAllMembers();
	}
	
	// ✅ 회원 목록 조회 (페이지네이션 + 검색)
	@Override
	public Map<String, Object> selectMemberList(int cp, String keyword, String searchType) {
	    // 파라미터를 Map으로 전달
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("keyword", keyword);
	    paramMap.put("searchType", searchType);
	    
	    // 검색 조건에 맞는 회원 수 조회
	    int listCount = mapper.getMemberCount(paramMap);
	    
	    // Pagination 객체 생성 (페이지당 10개)
	    Pagination pagination = new Pagination(cp, listCount, 10);
	    
	    // RowBounds 설정
	    int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
	    RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
	    
	    // 회원 목록 조회
	    List<Member> memberList = mapper.selectMemberListPaging(paramMap, rowBounds);
	    
	    // 결과 반환
	    Map<String, Object> map = new HashMap<>();
	    map.put("pagination", pagination);
	    map.put("memberList", memberList);
	    
	    return map;
	}



	// 굿즈 상품 조회
	@Override
	public List<Goods> selectProducts(String sort) {
		return mapper.selectProducts(sort);
	}
	
	// ✅ 상품 목록 조회 (페이징 + 검색 + 필터)
	@Override
	public Map<String, Object> selectGoodsList(int cp, String keyword, String goodsStatus, 
	                                              String startDate, String endDate, String sort) {
	    // 파라미터를 Map으로 전달
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("keyword", keyword);
	    paramMap.put("goodsStatus", goodsStatus);
	    paramMap.put("startDate", startDate);
	    paramMap.put("endDate", endDate);
	    paramMap.put("sort", sort);
	    
	    // 검색 조건에 맞는 상품 수 조회
	    int listCount = mapper.getGoodsCount(paramMap);
	    
	    // Pagination 객체 생성 (페이지당 10개)
	    Pagination pagination = new Pagination(cp, listCount, 10);
	    
	    // RowBounds 설정
	    int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
	    RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
	    
	    // 상품 목록 조회
	    List<Goods> goodsList = mapper.selectGoodsListPaging(paramMap, rowBounds);
	    
	    // 결과 반환
	    Map<String, Object> map = new HashMap<>();
	    map.put("pagination", pagination);
	    map.put("goodsList", goodsList);
	    
	    return map;
	}
	
	// ✅ 선택 상품 재입고
	@Override
	public int restockGoods(List<String> boardNos, int stock) {
	    int count = 0;
	    for (String boardNo : boardNos) {
	        Map<String, Object> params = new HashMap<>();
	        params.put("boardNo", boardNo);
	        params.put("stock", stock);
	        count += mapper.restockGoods(params);
	    }
	    return count;
	}




	// ✅ 선택 상품 삭제
	@Override
	public int deleteGoods(List<String> boardNos) {
	    int count = 0;
	    for (String boardNo : boardNos) {
	        count += mapper.deleteGoods(boardNo);
	    }
	    return count;
	}

	// ✅ 선택 상품 품절 처리
	@Override
	public int soldOutGoods(List<String> boardNos) {
	    int count = 0;
	    for (String boardNo : boardNos) {
	        count += mapper.soldOutGoods(boardNo);
	    }
	    return count;
	}

	
	// ✅ 신고 목록 조회 (페이징 + 검색 + 필터)
	@Override
	public Map<String, Object> selectReportList(String keyword, String reportType, String reportStatus, 
	                                              String startDate, String endDate, int cp) {
	    // 파라미터를 Map으로 전달
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("keyword", keyword);
	    paramMap.put("reportType", reportType);
	    paramMap.put("reportStatus", reportStatus);
	    paramMap.put("startDate", startDate);
	    paramMap.put("endDate", endDate);
	    
	    // 검색 조건에 맞는 신고 수 조회
	    int listCount = mapper.getReportListCount(paramMap);
	    
	    // Pagination 객체 생성
	    Pagination pagination = new Pagination(cp, listCount, 16);
	    
	    // RowBounds 설정
	    int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
	    RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
	    
	    // 신고 목록 조회
	    List<Report> reportList = mapper.selectReportList(paramMap, rowBounds);
	    
	    // 결과 반환
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
	public int updateReportResult(Map<String, Object> report) {
		
		return mapper.updateReportResult(report);
	}

	// (회원) 회원id 또는 닉네임으로 검색
	@Override
	public List<Member> searchMember(String keyword) {
		return mapper.searchMember(keyword);
	}

	// (신고) 회원id 또는 게시글로 검색
	@Override
	public List<Report> searchReport(String keyword) {
		return mapper.searchReport(keyword);
	}

	// (굿즈) 상품명으로 검색
	@Override
	public List<Goods> searchGoods(String keyword) {
		return mapper.searchGoods(keyword);
	}

	// 굿즈 거래내역 상품명 또는 구매자명 검색
	@Override
	public List<OrderDto> searchOrder(String keyword) {
		return mapper.searchOrder(keyword);
	}

	// 굿즈 거래내역 조회
	@Override
	public List<OrderDto> selectGoodsOrder(String sort) {
		return mapper.selectGoodsOrder(sort);
	}
	
	// ✅ 거래 내역 조회 (페이징 + 검색 + 필터)
	@Override
	public Map<String, Object> selectOrderList(int cp, String keyword, String deliveryStatus, 
	                                              String startDate, String endDate, String sort) {
	    // 파라미터를 Map으로 전달
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("keyword", keyword);
	    paramMap.put("deliveryStatus", deliveryStatus);
	    paramMap.put("startDate", startDate);
	    paramMap.put("endDate", endDate);
	    paramMap.put("sort", sort);
	    
	    // 검색 조건에 맞는 거래 수 조회
	    int listCount = mapper.getOrderCount(paramMap);
	    
	    // Pagination 객체 생성 (페이지당 10개)
	    Pagination pagination = new Pagination(cp, listCount, 10);
	    
	    // RowBounds 설정
	    int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
	    RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
	    
	    // 거래 목록 조회
	    List<OrderDto> orderList = mapper.selectOrderListPaging(paramMap, rowBounds);
	    
	    // 결과 반환
	    Map<String, Object> map = new HashMap<>();
	    map.put("pagination", pagination);
	    map.put("orderList", orderList);
	    
	    return map;
	}

	
}
