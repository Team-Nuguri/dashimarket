package edu.og.project.goods.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.goods.model.dao.CartMapper;
import edu.og.project.goods.model.dao.GoodsMapper;
import edu.og.project.goods.model.dto.Goods;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartMapper mapper;
	@Autowired
	private GoodsMapper goodsMapper;
	
	// 장바구니 삽입
	@Override
	public int cartInsert(Map<String, Object> paramMap) {
		
		
		// 현재 품목이 장바구니에 있는지 먼저 확인
		int result = mapper.selectCart(paramMap);
		
		if(result == 0) { // 조회 결과 0 이면 insert
			
			result = mapper.insertCart(paramMap);
			
		}else { // 조회결과 있다면 장바구니 업데이트
			result = mapper.updateCart(paramMap);
			
		}
		
		
		
		return result;
	}
	
	
	// 장바구니 굿즈 목록 조회
	@Override
	public List<Goods> selectCartGoodsList(int i) {
		
		
		
		return goodsMapper.selectCartGoodsList(i);
	}

	
	
	// 장바구니 삭제
	@Override
	public int cartItemDelete(Map<String, Object> paramMap) {
		
		return mapper.cartItemDelete(paramMap);
	}

}
