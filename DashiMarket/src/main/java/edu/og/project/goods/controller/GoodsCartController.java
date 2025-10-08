package edu.og.project.goods.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.goods.model.dto.Goods;
import edu.og.project.goods.model.service.CartService;

@Controller
public class GoodsCartController {
	
	@Autowired
	private CartService service;
	
	@PostMapping("/shoppingcart")
	@ResponseBody
	public int cartInsert(
			@RequestBody Map<String, Object> paramMap
			, RedirectAttributes ra
			// 세션 로그인 번호
			) {
		
		
		paramMap.put("memberNo", 1);
		System.out.println(paramMap);
		
		
		return service.cartInsert(paramMap);
		
	}
	
	
	// 장바구니 화면 전환
	@GetMapping(value= "/goods/shoppingcart", produces="application/html; charset=UTF-8")
	public String cartForward(
			// 세션에서 로그인 멤버 번호 얻어오기
			Model model
			) {
		
		List<Goods> cartGoodsList = service.selectCartGoodsList(1);
		
		model.addAttribute("cartGoodsList", cartGoodsList);
		
		return "/cartPage/cart";
	}
	
	// 장바구니 삭제
	@DeleteMapping("/goods/shoppingcart/delete")
	@ResponseBody
	public int cartItemDelete(@RequestBody Map<String, Object> paramMap
			// 세션에서 로그인 한 회원 번호 얻어오기
					
			) {
		
		paramMap.put("memberNo", 1);
		
		return service.cartItemDelete(paramMap);
		
	}
}
