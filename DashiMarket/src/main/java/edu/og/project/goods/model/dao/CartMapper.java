package edu.og.project.goods.model.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper {
	
	// 현재 장바구니에 품목이 있는지 확인
	int selectCart(Map<String, Object> paramMap);
	
	// 장바구니 추가
	int insertCart(Map<String, Object> paramMap);
	
	// 장바구니 수정
	int updateCart(Map<String, Object> paramMap);
	
	
	// 장바구니 삭제
	int cartItemDelete(Map<String, Object> paramMap);

}
