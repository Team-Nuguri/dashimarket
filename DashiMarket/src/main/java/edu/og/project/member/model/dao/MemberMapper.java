package edu.og.project.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.common.dto.Member;

@Mapper
public interface MemberMapper {

	Member login(Member inputMember);
	
	int signUp(Member inputMember);
	
	
	// 신고 게시글 올린 유저 조회
	Member selectMemberInfo(String targetNo);
}
