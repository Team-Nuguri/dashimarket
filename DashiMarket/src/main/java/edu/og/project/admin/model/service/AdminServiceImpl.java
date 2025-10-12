package edu.og.project.admin.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
