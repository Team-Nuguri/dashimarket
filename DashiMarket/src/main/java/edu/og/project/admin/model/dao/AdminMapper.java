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

	// 전체 회원 조회
	List<Member> selectAllMembers();
	
	// 전체 회원 수
	int getMemberCount();

	// 회원 목록 조회 (페이징)
	List<Member> selectMemberListPaging(RowBounds rowBounds);

	// 굿즈 상품 조회
	List<Goods> selectProducts(String sort);
	
	// 전체 상품 수
	int getGoodsCount();

	// 상품 목록 조회 (페이징)
	List<Goods> selectGoodsListPaging(@Param("sort") String sort, RowBounds rowBounds);
	
	// 오늘 신고된 수
	Integer getTodayReportCount();

	// 신고
	public int getReportListCount(String keyword, String reportResult);

	public List<Report> selectReportList(String keyword, String reportResult, RowBounds rowBounds);

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

	// 전체 거래 수
	int getOrderCount();

	// 거래 목록 조회 (페이징)
	List<OrderDto> selectOrderListPaging(@Param("sort") String sort, RowBounds rowBounds);

	

}
