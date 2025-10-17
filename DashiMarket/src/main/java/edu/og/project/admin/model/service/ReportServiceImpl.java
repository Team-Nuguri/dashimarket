package edu.og.project.admin.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.admin.model.dao.ReportMapper;
import edu.og.project.admin.model.dto.Report;
import edu.og.project.chatting.model.dao.ChattingMapper;
import edu.og.project.common.dto.Member;
import edu.og.project.member.model.dao.MemberMapper;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private ReportMapper mapper;
	
	
	// 게시글 올린 유저 정보 조회
	@Override
	public Member selectMemberInfo(String targetNo) {
		
		
		return memberMapper.selectMemberInfo(targetNo);
	}
	
	
	// 신고삽입
	@Override
	public int insertReport(Report report) {
		
		
		int result = mapper.selectReportCheck(report);
		
		if(result == 0) {
			result =mapper.insertReport(report);
			
		}else {
			// 임의 숫자 반환 이미 신고 했음
			return -1;
		}
		
		return result;
	}
	

	// 채팅방 신고 당한 유저 정보 조회
	@Override
	public Member selectChattingInfo(Map<String, Integer> map) {
		return memberMapper.selectChattingInfo(map);
	}

}
