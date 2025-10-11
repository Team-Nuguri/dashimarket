package edu.og.project.mypage.model;

import org.springframework.web.multipart.MultipartFile;

import edu.og.project.common.dto.Member;

public interface mypageService {
    
	// 💡 기존의 updateInfo 메서드는 제거하거나, 아래 메서드로 대체합니다.
    
    /** 회원 정보 및 프로필 이미지 수정
     * @param updateMember 수정할 회원 정보 (닉네임, 전화번호, 주소 등)
     * @param loginMember 현재 로그인 회원 정보 (기존 파일 경로 등 활용)
     * @param deleteCheck 이미지 변경/삭제 상태 (0:삭제, 1:변경, -1:초기)
     * @param profileImage 업로드된 이미지 파일
     * @return result (성공: 1 이상, 실패: 0)
     */
    int updateProfile(Member updateMember, Member loginMember, 
                      int deleteCheck, MultipartFile profileImage) throws Exception; 
                      // 파일 I/O 처리를 위해 Exception 던지도록 추가
}