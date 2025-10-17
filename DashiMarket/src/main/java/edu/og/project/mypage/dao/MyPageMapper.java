package edu.og.project.mypage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.og.project.common.dto.Member;
import edu.og.project.joonggo.model.dto.Joonggo;

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
	List<Map<String, Object>> selectGoods(Map<String, Object> paramMap);
	
	  // 굿즈 거래 - 전체 개수 조회
    int getGoodsListCount(Map<String, Object> paramMap);
    
    // 굿즈 거래 - 목록 조회 (RowBounds 사용)
    List<Map<String, Object>> selectGoodsList(Map<String, Object> paramMap, RowBounds rowBounds);
    
    /**
     * 구매 확정
     */
    int confirmPurchase(Map<String, Object> paramMap);

    // 마이페이지 나의 위시리스트 조회 (KJK)
	List<Joonggo> selectJoonggoWishList(int memberNo);

	// 마이페이지 나의 위시리스트 삭제 (KJK)
	int deleteJoonggoWishList(int memberNo, String boardNo);
	
	// 중고 거래
	int getOrderListCount(Map<String, Object> paramMap);

	List<Map<String, Object>> selectOrderList(Map<String, Object> paramMap, RowBounds rowBounds);
		
		
}