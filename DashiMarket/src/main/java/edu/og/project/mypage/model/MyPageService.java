package edu.og.project.mypage.model;

import org.springframework.web.multipart.MultipartFile;
import edu.og.project.common.dto.Member;

public interface MyPageService {
    
    /**
     * 회원 정보 조회 (비밀번호 제외)
     */
    Member selectMember(int memberNo);
    
    /**
     * 회원 정보 조회 (비밀번호 포함) - 비밀번호 확인용
     */
    Member selectMemberWithPassword(int memberNo);
    
    /**
     * 프로필 수정
     */
    int updateProfile(Member updateMember, Member loginMember, int deleteCheck, MultipartFile profileImage) throws Exception;

	/** 회원 탈퇴
	 * @param member
	 * @param memberNo 
	 * @return result
	 */
	int secession(String memberPw, int memberNo);
	

	/**
	 * 비밀번호 일치 여부 확인
	 * @param memberNo 회원 번호
	 * @param inputPassword 사용자가 입력한 비밀번호 (암호화 전)
	 * @return 일치하면 true, 불일치하면 false
	 */
	boolean checkPassword(int memberNo, String inputPassword);
	
	
	/**
	 * 회원 탈퇴 사유를 관리자에게 이메일로 전송
	 * @param loginMember 로그인한 회원 정보
	 * @param reasons 체크박스로 선택한 탈퇴 사유 목록
	 * @param detailedReason 상세 사유
	 * @return 이메일 전송 성공 시 true, 실패 시 false
	 */
	boolean sendSecessionEmail(Member loginMember, java.util.List<String> reasons, String detailedReason);

}