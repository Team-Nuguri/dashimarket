package edu.og.project.admin.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import edu.og.project.admin.model.dto.Report;

@Mapper
public interface ReportMapper {
	
	public int getReportListCount(String keyword, String reportResult);

	public List<Report> selectReportList(String keyword, String reportResult, RowBounds rowBounds);

	public Report selectReportDetail(int reportNo);

	int updateReportResult(@Param("reportNo") int reportNo, @Param("resultType") String resultType);
}
