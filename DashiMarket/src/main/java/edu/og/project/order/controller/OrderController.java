package edu.og.project.order.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.og.project.common.dto.Member;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.goods.model.service.GoodsService;
import edu.og.project.order.model.dto.OrderDto;
import edu.og.project.order.model.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class OrderController {
	
	@Autowired
	private OrderService service;
	
	@Autowired
	private GoodsService service2;
	
	
	@Value("${my.goodsInfo.webpath}")
	private String iWebPath;
	
	@Value("${portone.api.key}")
	private String storeId;
	
	@Value("${portone.api.secret}")
	private String secret;
	
	@Value("${portone.channel.key}")
	private String channelKey;
	
	
	
	
	@PostMapping("/goods/order")
	public String goodsOrderItem(
			@ModelAttribute OrderDto orderItems,
			Model model,
			HttpServletRequest req
			
			) {
		
		log.info("데이터 확인(아이템 리스트) :" + orderItems);
		
		
		List<Goods> orderList = new ArrayList<>();
		for (int i = 0; i < orderItems.getOrderItems().size(); i++) {
			
			String boardNo = orderItems.getOrderItems().get(i).getBoardNo();
			int quantity = orderItems.getOrderItems().get(i).getQuantity();
			
			orderList.add(service2.selectGoodsDetail(boardNo));
			
			orderList.get(i).setQuantity(quantity);
			
			
		}
		
		log.info("주문 상품 추가 확인  :" + orderList);
		String path = null;
		String referer = req.getHeader("Referer");
		if(orderList.size()>0) {
			model.addAttribute("orderList", orderList);
			model.addAttribute("totalPrice", orderItems.getTotalPrice());
			path = "/orderPage/order";
			
		}else {
			model.addAttribute("message", "오류 발생 ㅜㅠ ");
			path = "redirect:"+ referer;
		}
		
		
		return path;
	}
	
	
	
	// 결제 클릭 시 주문 테이블 insert
	
	@PostMapping("/goods/payment")
	@ResponseBody
	public Map<String, Object> orderItemInsert(
			@RequestBody Map<String, Object> paramMap,
			@SessionAttribute("loginMember") Member loginMember
			) {
		
		
		// 일단 오더 테이블 인서트 하고 주문번호 반환
		// 채널키 상점키 등등 같이 반환
		
		
		paramMap.put("memberNo", loginMember.getMemberNo());
		
		System.out.println(paramMap);
		
		Map<String, Object> map = service.insertOrder(paramMap);
		map.put("storeId", storeId);
		map.put("secret", secret);
		map.put("channelKey", channelKey);
		map.put("totalPrice", paramMap.get("totalPrice"));
		
		
		return map;
	}
	
	// 결제 취소 시 주문 테이블 삭제
	@GetMapping("/goods/payment/fail")
	public String orderDelete(String orderNo,
			HttpServletRequest req
			) {
		
		service.orderDelete(Long.parseLong(orderNo) );
		
		String referer = req.getHeader("Referer");
		
		return "redirect:" + referer;
		
	}
	
	
	
	@PostMapping("/goods/payment/complete")
	@ResponseBody
	public Map<String, Object> paymentComplete(
			@RequestBody Map<String, Object> paramMap
			) {
		
		System.out.println(paramMap);
		
		paramMap.put("orderNo", Long.parseLong((String) paramMap.get("orderNo")));
		
		Map<String, Object> map = new HashMap<>();
		
		int result = service.paymentComplete(paramMap);
		
		if(result != 0) {
			
			map.put("verified", true);
			map.put("message", "결제가 성공적으로 완료되었으며, 주문이 최종 승인되었습니다.");	
		}else {
			map.put("verified", false);
			map.put("message", "주문 처리 중 오류가 발생");
		}
		
		
		return map;
		
	}
	
	
	// 최종 결제 확인 시 주문 완료 페이지로
	@GetMapping("/goods/orderComplete")
	public String forWardOrderComplete(
			String orderNo,
			Model model,
			@SessionAttribute("loginMember")Member loginMember
			) {
		
		
		
		OrderDto orderCompleteList = service.selectOrderCompleteList(orderNo);
		
		orderCompleteList.setMemberName(loginMember.getMemberName());
		
		model.addAttribute("orderCompleteList", orderCompleteList);
		
		return "/orderPage/orderComplete";
		
	}
	
}
