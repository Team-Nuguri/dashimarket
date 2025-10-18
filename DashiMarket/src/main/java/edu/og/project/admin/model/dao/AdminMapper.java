package edu.og.project.admin.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.repository.query.Param;

import edu.og.project.admin.model.dto.Report;
import edu.og.project.common.dto.Member;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.order.model.dto.OrderDto;

@Mapper
public interface AdminMapper {

	// 오늘 가입자 수
	Integer selectTodayJoincount();

	// 총 회원 수
	Integer selectTotalUsercount();
	
	// 오늘 신고된 수
	Integer getTodayReportCount();
	
	// 배송전
	Integer getBeforeDelivery();

	// 배송중
	Integer getInDelivery();

	// 배송완료
	Integer getDeliveryCompleted();

	// 전체 회원 조회
	List<Member> selectAllMembers();
	
	// 검색 조건에 맞는 회원 수
	int getMemberCount(Map<String, Object> paramMap);

	// 회원 목록 조회 (페이징 + 검색)
	List<Member> selectMemberListPaging(Map<String, Object> paramMap, RowBounds rowBounds);


	// 굿즈 상품 조회
	List<Goods> selectProducts(String sort);
	
	// 검색 조건에 맞는 상품 수
	int getGoodsCount(Map<String, Object> paramMap);

	// 상품 목록 조회 (페이징 + 검색 + 필터)
	List<Goods> selectGoodsListPaging(Map<String, Object> paramMap, RowBounds rowBounds);
	
	// 선택 상품 재입고
	int restockGoods(Map<String, Object> params);

	// 선택 상품 삭제
	int deleteGoods(String boardNo);

	// 선택 상품 품절 처리
	int soldOutGoods(String boardNo);


	// 신고
	
	// ✅ 신고 수 조회 (Map 버전으로 수정)
    int getReportListCount(Map<String, Object> paramMap);

    // ✅ 신고 목록 조회 (Map 버전으로 수정)
    List<Report> selectReportList(Map<String, Object> paramMap, RowBounds rowBounds);

	public Report selectReportDetail(int reportNo);

	int updateReportResult(Map<String, Object> report);
	 

	// (회원) 회원id 또는 닉네임으로 검색
	List<Member> searchMember(String keyword);

	// (신고) 회원id 또는 게시글로 검색
	List<Report> searchReport(String keyword);

	// (굿즈) 상품명으로 검색
	List<Goods> searchGoods(String keyword);

	// 굿즈 거래내역 상품명 또는 구매자명 검색
	List<OrderDto> searchOrder(String keyword);

	// 굿즈 거래내역 조회
	List<OrderDto> selectGoodsOrder(String sort);

	// 검색 조건에 맞는 거래 수
	int getOrderCount(Map<String, Object> paramMap);

	// 거래 목록 조회 (페이징 + 검색 + 필터)
	List<OrderDto> selectOrderListPaging(Map<String, Object> paramMap, RowBounds rowBounds);


}
