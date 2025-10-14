package edu.og.project.admin.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.common.dto.Member;
import edu.og.project.goods.model.dto.Goods;
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

	/** 전체 회원 조회
	 * @return List<Member>
	 */
	List<Member> selectAllMembers();

	/** 굿즈 상품 조회
	 * @param sort 
	 * @return products
	 */
	List<Goods> selectProducts(String sort);

	// 신고 
	Map<String, Object> selectReportList(String keyword, String reportResult, int cp);

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


}
