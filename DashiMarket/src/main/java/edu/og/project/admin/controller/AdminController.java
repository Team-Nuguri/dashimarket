package edu.og.project.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.og.project.admin.model.service.AdminService;
import edu.og.project.common.dto.Member;
import edu.og.project.goods.model.dto.Goods;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService service;
	
	// 회원 조회 페이지 요청
	@GetMapping("/main")
	public String main() {
		return "admin/admin_user";
	}
	
	// 오늘 가입자 수 + 총 회원 수 조회
	@GetMapping("/stats")
	@ResponseBody
	public Map<String, Integer> getTodayStats() {
		
		Map<String, Integer> result = new HashMap<>();
		result.put("todayJoinCount", service.getTodayJoinCount());
		result.put("totalUserCount", service.getTotalUserCount());
		return result;
	}
	
	// 전체 회원 조회
	@GetMapping("/members")
	@ResponseBody
	public List<Member> selectAllMembers(){
		return service.selectAllMembers();
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
	
	// 상품 정보 조회
	@GetMapping("/product")
	@ResponseBody
	public List<Goods> selectProducts(@RequestParam(required = false) String sort){
		return service.selectProducts(sort);
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

	// 데이터 - 중고 상품별 페이지 요청
	@GetMapping("/data/goods")
	public String dataGoods() {

		return "admin/admin_dataGoods";
	}
		
	
	
	
	
	
	
	
		
}
