package edu.og.project.admin.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.admin.model.dto.Report;

public interface AdminService {

	
	Map<String, Object> selectReportList(String keyword, String reportResult, int cp);

	Report selectReportDetail(int reportNo);

	int updateReportResult(int reportNo, String resultType);

}
