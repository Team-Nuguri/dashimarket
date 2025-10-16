package edu.og.project.admin.model.service;

import java.util.Map;

import edu.og.project.admin.model.dto.Report;
import edu.og.project.common.dto.Member;

public interface ReportService {
	
	/** 게시글 올린 유저 정보 조회
	 * @param boardNo
	 * @return Member
	 */
	Member selectMemberInfo(String targetNo);
	
	
	/** 신고 테이블 삽입
	 * @param report
	 * @return 성공 or 실패 1 0
	 */
	int insertReport(Report report);


	/** 채팅방 신고 당한 유저 정보 조회
	 * @param map
	 * @return
	 */
	Member selectChattingInfo(Map<String, Integer> map);

}
