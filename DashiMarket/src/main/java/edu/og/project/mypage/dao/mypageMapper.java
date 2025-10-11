package edu.og.project.mypage.dao;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.common.dto.Member;

//주의: XML의 namespace와 일치하는 인터페이스가 존재해야 합니다.
@Mapper
public interface mypageMapper { 
 // XML의 <update id="updateProfile">과 매핑됩니다.
 // Service에서 이 메서드를 호출할 때 Member 객체를 파라미터로 넘기면,
 // 해당 객체의 모든 필드(memberNo, memberNickname, profilePath 등)가 쿼리에 사용됩니다.
 public int updateProfile(Member updateMember);
}