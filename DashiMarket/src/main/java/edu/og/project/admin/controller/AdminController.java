package edu.og.project.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.og.project.admin.model.dto.Report;
import edu.og.project.admin.model.service.AdminService;

import edu.og.project.common.dto.Member;
import edu.og.project.goods.model.dto.Goods;
import edu.og.project.order.model.dto.OrderDto;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService service;

	// ✅ 회원 조회 (검색 기능 추가)
	@GetMapping("/main")
	public String main(
	    @RequestParam(value = "cp", defaultValue = "1") int cp,
	    @RequestParam(value = "keyword", required = false) String keyword,
	    @RequestParam(value = "searchType", required = false) String searchType,
	    Model model
	) {
	    Map<String, Object> map = service.selectMemberList(cp, keyword, searchType);
	    model.addAttribute("memberList", map.get("memberList"));
	    model.addAttribute("pagination", map.get("pagination"));
	    return "admin/admin_user";
	}


	// 오늘 가입자 수 + 총 회원 수 조회 + 오늘 신고된 수
	@GetMapping("/stats")
	@ResponseBody
	public Map<String, Integer> getTodayStats() {

		Map<String, Integer> result = new HashMap<>();
		result.put("todayJoinCount", service.getTodayJoinCount());
		result.put("totalUserCount", service.getTotalUserCount());
		result.put("todayReportCount", service.getTodayReportCount());
		return result;
	}

	// 전체 회원 조회
	@GetMapping("/members")
	@ResponseBody
	public List<Member> selectAllMembers() {
		return service.selectAllMembers();
	}
	
	// 회원 검색
	@GetMapping("/main/search")
	@ResponseBody
	public List<Member> searchMember(@RequestParam String keyword){
		return service.searchMember(keyword);
	}

	// ✅ 통합 신고 페이지 (필터 기능 추가)
	@GetMapping("/report")
	public String report(
	    @RequestParam(value = "keyword", required = false) String keyword,
	    @RequestParam(value = "reportType", required = false) String reportType,
	    @RequestParam(value = "reportStatus", required = false) String reportStatus,
	    @RequestParam(value = "startDate", required = false) String startDate,
	    @RequestParam(value = "endDate", required = false) String endDate,
	    @RequestParam(value = "cp", defaultValue = "1") int cp, 
	    Model model
	) {
	    Map<String, Object> map = service.selectReportList(keyword, reportType, reportStatus, startDate, endDate, cp);
	    model.addAttribute("reportList", map.get("reportList"));
	    model.addAttribute("pagination", map.get("pagination"));
	    
	    return "admin/total_report";
	}

	// 신고 상세 페이지 요청
	@GetMapping("/report/detail/{reportNo}")
	public String reportDetail(@PathVariable("reportNo") int reportNo, Model model) {

		Report report = service.selectReportDetail(reportNo);
		model.addAttribute("report", report);

		return "admin/total_report_detail";
	}

	// 신고 처리완료
	@PostMapping("/report/updateResult")
	@ResponseBody
	public String updateReportResult(@RequestBody List<Map<String, Object>> reportNoList) {
		try {
			for (Map<String, Object> report : reportNoList) {
				// int reportNo = Integer.parseInt(report.get("reportNo").toString());
				// String resultType = report.get("resultType").toString();
				// System.out.println(reportNo);
				// System.out.println(resultType);
				service.updateReportResult(report);
				
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
	
	// 신고 검색
	@GetMapping("/report/search")
	@ResponseBody
	public List<Report> searchReport(@RequestParam String keyword){
		return service.searchReport(keyword);
	}

	// ✅ 상품 관리 (검색 + 필터 추가)
	@GetMapping("/goods")
	public String goods(
	    @RequestParam(value = "cp", defaultValue = "1") int cp,
	    @RequestParam(value = "keyword", required = false) String keyword,
	    @RequestParam(value = "goodsStatus", required = false) String goodsStatus,
	    @RequestParam(value = "startDate", required = false) String startDate,
	    @RequestParam(value = "endDate", required = false) String endDate,
	    @RequestParam(value = "sort", required = false) String sort,
	    Model model
	) {
	    Map<String, Object> map = service.selectGoodsList(cp, keyword, goodsStatus, startDate, endDate, sort);
	    model.addAttribute("goodsList", map.get("goodsList"));
	    model.addAttribute("pagination", map.get("pagination"));
	    return "admin/admin_goods";
	}
	
	// ✅ 선택 상품 삭제
	@PostMapping("/goods/delete")
	@ResponseBody
	public String deleteGoods(@RequestBody List<String> boardNos) {
	    try {
	        int result = service.deleteGoods(boardNos);
	        return result > 0 ? "success" : "fail";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "fail";
	    }
	}

	// ✅ 선택 상품 품절 처리
	@PostMapping("/goods/soldout")
	@ResponseBody
	public String soldOutGoods(@RequestBody List<String> boardNos) {
	    try {
	        int result = service.soldOutGoods(boardNos);
	        return result > 0 ? "success" : "fail";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "fail";
	    }
	}
	
	// ✅ 5. 선택 상품 재입고
	@PostMapping("/goods/restock")
	@ResponseBody
	public String restockGoods(@RequestBody Map<String, Object> data) {
	    try {
	        @SuppressWarnings("unchecked")
	        List<String> boardNos = (List<String>) data.get("boardNos");
	        int stock = (int) data.get("stock");
	        
	        int result = service.restockGoods(boardNos, stock);
	        return result > 0 ? "success" : "fail";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "fail";
	    }
	}


	
	// 상품 정보 조회
	@GetMapping("/product")
	@ResponseBody
	public List<Goods> selectProducts(@RequestParam(required = false) String sort) {
		return service.selectProducts(sort);
	}
	
	// 상품명으로 검색
	@GetMapping("/goods/search")
	@ResponseBody
	public List<Goods> searchGoods(@RequestParam String keyword){
		return service.searchGoods(keyword);
	}


	// ✅ 거래 내역 (검색 + 필터 추가)
	@GetMapping("/order")
	public String order(
	    @RequestParam(value = "cp", defaultValue = "1") int cp,
	    @RequestParam(value = "keyword", required = false) String keyword,
	    @RequestParam(value = "deliveryStatus", required = false) String deliveryStatus,
	    @RequestParam(value = "startDate", required = false) String startDate,
	    @RequestParam(value = "endDate", required = false) String endDate,
	    @RequestParam(value = "sort", required = false) String sort,
	    Model model
	) {
	    Map<String, Object> map = service.selectOrderList(cp, keyword, deliveryStatus, startDate, endDate, sort);
	    model.addAttribute("orderList", map.get("orderList"));
	    model.addAttribute("pagination", map.get("pagination"));
	    return "admin/admin_goodsOrder";
	}

	
	// 굿즈 거래내역 조회
	@GetMapping("/purchase")
	@ResponseBody
	public List<OrderDto> selectGoodsOrder(@RequestParam(required = false) String sort){
		return service.selectGoodsOrder(sort);
	}
	
	
	// 굿즈 거래내역 구매자명 또는 상품명으로 검색
	@GetMapping("/order/search")
	@ResponseBody
	public List<OrderDto> searchOrder(@RequestParam String keyword){
		return service.searchOrder(keyword);
	}
	
	
//	// 데이터 - 이용자 수 페이지 요청
//	@GetMapping("/data/user")
//	public String dataUser() {
//
//		return "admin/admin_dataUser";
//	}
//
//	// 데이터 - 중고 상품별 페이지 요청
//	@GetMapping("/data/goods")
//	public String dataGoods() {
//
//		return "admin/admin_dataGoods";
//	}

}
