package edu.og.project.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.og.project.admin.model.dto.Report;
import edu.og.project.admin.model.service.ReportService;
import edu.og.project.common.dto.Member;

@Controller
public class ReportController {
	
	@Autowired
	private ReportService service;
	
	
	// 신고 팝업
	@GetMapping("/{boardType}/report/{targetNo}")
	public String reportForward(
			@PathVariable("targetNo") String targetNo,
			@PathVariable("boardType") String boardType
			, Model model
			
			) {
		
		// 해당 게시글 올린 회원 정보 조회
		Member member = service.selectMemberInfo(targetNo);
		
		model.addAttribute("member", member);
		
		
		return "/common/report";
	}
	
	
	// 신고 삽입
	
	@PostMapping("/{boardType}/report/{targetNo}")
	@ResponseBody
	public int reportInsert(
			@PathVariable("targetNo") String reportTargetId,
			@PathVariable("boardType") String reportTarget,
			@SessionAttribute("loginMember") Member loginMember,
			@RequestBody Report report) {
		
		
		report.setReportTarget(reportTarget);
		report.setReportTargetId(reportTargetId);
		report.setReportMemberNo(loginMember.getMemberNo());
		
		//System.out.println(report);
		
		
		
		
		
		return service.insertReport(report);
	}

}
