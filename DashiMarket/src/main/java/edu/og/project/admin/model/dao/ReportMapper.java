package edu.og.project.admin.model.dao;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.admin.model.dto.Report;

@Mapper
public interface ReportMapper {
	
	// 신고 삽입
	int insertReport(Report report);
	
	// 로그인 한 회원이 신고한 적 있는지 
	int selectReportCheck(Report report);
	
	
	

}
