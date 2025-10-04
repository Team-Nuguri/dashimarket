package edu.og.project.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {
	
	// 회원 조회 페이지 요청
	@GetMapping("/main")
	public String main() {
		
		return "admin/admin_user";
	}
	
	// 통합 신고 페이지 요청
	@GetMapping("/report")
	public String report() {
		
		return "admin/total_report";
	}
	
	// 상품 관리 페이지 요청
	@GetMapping("/goods")
	public String goods() {
		
		return "admin/admin_goods";
	}
	
	// 굿즈 거래 페이지 요청
	@GetMapping("/order")
	public String order() {
		
		return "admin/admin_goodsOrder";
	}
	
	// 데이터 - 이용자 수 페이지 요청
	@GetMapping("/data/user")
	public String dataUser() {
		
		return "admin/admin_dataUser";
	}
		
	// 통합 신고 페이지 요청
	@GetMapping("/data/goods")
	public String dataGoods() {
		
		return "admin/admin_dataGoods";
	}		
		
	
	
	
	
	
	
	
		
}
