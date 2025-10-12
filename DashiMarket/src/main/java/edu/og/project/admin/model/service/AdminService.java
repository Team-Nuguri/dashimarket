package edu.og.project.admin.model.service;

import java.util.List;

import edu.og.project.common.dto.Member;
import edu.og.project.goods.model.dto.Goods;

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
	 * @return List<Goods>
	 */
	List<Goods> selectProducts(String sort);

}
