package edu.og.project.member.model.service;

import java.util.Map;

import edu.og.project.common.dto.Member;

public interface EmailService {

	int signUp(String email, String title);
	
		String createAuthKey();
		
	int checkAuthKey (Map<String, Object> paramMap);
	
	 /** ⭐️ 추가: 회원 탈퇴 사유 관리자에게 전송
     * @param loginMember 로그인 회원 정보
     * @param reasons 체크된 탈퇴 사유 목록
     * @param detailedReason 상세 탈퇴 사유
     * @return 이메일 발송 성공 시 true
     */
    boolean sendSecessionReasonEmail(Member loginMember, java.util.List<String> reasons, String detailedReason);
}
