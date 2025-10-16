package edu.og.project.admin.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.common.dto.Member;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.order.model.dto.OrderDto;
import edu.og.project.admin.model.dto.Report;

public interface AdminService {

	/** 오늘 가입자 수
	 * @return result
	 */
	Integer getTodayJoinCount();

	/** 총 회원 수
	 * @return result
	 */
	Integer getTotalUserCount();
	
	/** 오늘 신고된 수
	 * @return result
	 */
	Integer getTodayReportCount();

	/** 전체 회원 조회
	 * @return List<Member>
	 */
	List<Member> selectAllMembers();
	
	/** 회원 목록 조회 (페이징 + 검색)
	 * @param cp 현재 페이지
	 * @param keyword 검색어
	 * @param searchType 검색 유형 (email, nickname, tel, secession)
	 * @return Map (memberList, pagination)
	 */
	Map<String, Object> selectMemberList(int cp, String keyword, String searchType);


	/** 굿즈 상품 조회
	 * @param sort 
	 * @return products
	 */
	List<Goods> selectProducts(String sort);
	
	// 상품 목록 조회 (페이징)
	Map<String, Object> selectGoodsList(int cp, String sort);

	/** 신고 목록 조회 (페이징 + 검색 + 필터)
	 * @param keyword 검색어
	 * @param reportType 신고 유형
	 * @param reportStatus 처리 상태
	 * @param startDate 신고일 시작
	 * @param endDate 신고일 종료
	 * @param cp 현재 페이지
	 * @return Map (reportList, pagination)
	 */
	Map<String, Object> selectReportList(String keyword, String reportType, String reportStatus, 
	                                      String startDate, String endDate, int cp);


	Report selectReportDetail(int reportNo);

	int updateReportResult(Map<String, Object> report);

	
	/** (회원) 회원id 또는 닉네임으로 검색
	 * @param keyword
	 * @return data
	 */
	List<Member> searchMember(String keyword);

	/** (신고) 회원id 또는 게시글로 검색
	 * @param keyword
	 * @return data
	 */
	List<Report> searchReport(String keyword);

	/** (굿즈) 상품명으로 검색
	 * @param keyword
	 * @return data
	 */
	List<Goods> searchGoods(String keyword);

	/** 굿즈 거래내역 검색 - 상품명 또는 구매자명
	 * @param keyword
	 * @return data
	 */
	List<OrderDto> searchOrder(String keyword);

	/** 굿즈 거래내역 조회
	 * @param sort
	 * @return orderList
	 */
	List<OrderDto> selectGoodsOrder(String sort);
	
	// 거래 내역 조회 (페이징)
	Map<String, Object> selectOrderList(int cp, String sort);
}
