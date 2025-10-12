package edu.og.project.admin.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.common.dto.Member;
import edu.og.project.goods.model.dto.Goods;

@Mapper
public interface AdminMapper {

	// 오늘 가입자 수
	Integer selectTodayJoincount();

	// 총 회원 수
	Integer selectTotalUsercount();

	// 전체 회원 조회
	List<Member> selectAllMembers();

	// 굿즈 상품 조회
	List<Goods> selectProducts(String sort);

}
