package edu.og.project.admin.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import edu.og.project.goods.model.service.GoodsService;

import org.springframework.web.bind.annotation.SessionAttribute;

import edu.og.project.admin.model.service.AdminService;
import edu.og.project.common.dto.Member;


@RequestMapping("/admin")
@Controller
public class AdminController {
	
	@Autowired
	private AdminService service;
	
	// 회원 조회 페이지 요청
	@GetMapping("/main")
	public String main() {
		
		return "admin/admin_user";
	}
	
	// 통합 신고 페이지 요청
	@GetMapping("/report")
	public String report(
			   @RequestParam(value = "keyword", required = false) String keyword,
			    @RequestParam(value = "reportResult", required = false) String reportResult,
			    @RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
			  Model model) {
		
		Map<String, Object> map = service.selectReportList(keyword, reportResult, cp);
		// Map<String, Object> reportList = service.selectReportList(keyword, reportResult, cp);
		model.addAttribute("reportList", map.get("reportList"));
	    // model.addAttribute("reportList", reportList);
	    model.addAttribute("pagination", map.get("pagination"));
		
	      
		 return "admin/total_report";
	}
	
	// 신고 상세 페이지 요청
	@GetMapping("/report/detail/{reportNo}")
	public String reportDetail(
	        @PathVariable("reportNo") int reportNo,
	        Model model) {

	    Report report = service.selectReportDetail(reportNo);
	    model.addAttribute("report", report);
 
	    return "admin/total_report_detail";
	}
	
	
	@PostMapping("/report/updateResult")
	@ResponseBody
	public String updateReportResult(@RequestBody List<Map<String, Object>> reportNoList) {
	    try {
	        for (Map<String, Object> report : reportNoList) {
	            int reportNo = Integer.parseInt(report.get("reportNo").toString());
	            String resultType = report.get("resultType").toString();
	            // System.out.println(reportNo);
	            // System.out.println(resultType);
	            service.updateReportResult(reportNo, resultType);
	        }
	        return "success";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "fail";
	    }
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
	
//	// 데이터 - 이용자 수 페이지 요청
//	@GetMapping("/data/user")
//	public String dataUser() {
//		
//		return "admin/admin_dataUser";
//	}
		
//	// 통합 신고 페이지 요청
//	@GetMapping("/data/goods")
//	public String dataGoods() {
//		
//		return "admin/admin_dataGoods";
//	}		
		
	
	
	
	
	
	
	
		
}
