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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.common.dto.Member;
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
			, @SessionAttribute("loginMember") Member loginMember
			) {
		
		
		paramMap.put("memberNo", loginMember.getMemberNo());
		System.out.println(paramMap);
		
		
		return service.cartInsert(paramMap);
		
	}
	
	
	// 장바구니 화면 전환
	@GetMapping(value= "/goods/shoppingcart", produces="application/html; charset=UTF-8")
	public String cartForward(
			Model model,
			@SessionAttribute("loginMember") Member loginMember
			) {
		
		List<Goods> cartGoodsList = service.selectCartGoodsList(loginMember.getMemberNo());
		
		System.out.println(cartGoodsList.size());
		
		if(cartGoodsList.size() != 0) {
			
			model.addAttribute("cartGoodsList", cartGoodsList);
		} else {
			model.addAttribute("cartGoodsList", 0);
		}
		
		return "/cartPage/cart";
	}
	
	// 장바구니 삭제
	@DeleteMapping("/goods/shoppingcart/delete")
	@ResponseBody
	public int cartItemDelete(@RequestBody Map<String, Object> paramMap
			,@SessionAttribute("loginMember") Member loginMember
			) {
		
		System.out.println(paramMap);
		
		paramMap.put("memberNo", loginMember.getMemberNo());
		
		return service.cartItemDelete(paramMap);
		
	}
}
