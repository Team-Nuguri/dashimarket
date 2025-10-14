package edu.og.project.mypage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import edu.og.project.common.dto.Member;

@Mapper
public interface MyPageMapper { 
    
    /**
     * 회원 정보 조회
     */
    Member selectMember(int memberNo);
    
    /**
     * 프로필 정보 업데이트
     */
    int updateProfile(Member updateMember);
    
    /**
     * 회원 정보 조회 (비밀번호 포함)
     */
    Member selectMemberWithPassword(int memberNo);
    
	// 회원 탈퇴
	public int secession(int memberNo);
	
	String selectEncPw(int memberNo);
	
	// 굿즈 거래
	List<Map<String, Object>> selectGoods(String memberEmail);
	
}