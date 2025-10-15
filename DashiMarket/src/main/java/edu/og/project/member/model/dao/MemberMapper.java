package edu.og.project.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.common.dto.Member;

@Mapper
public interface MemberMapper {

	Member login(Member inputMember);
	
	int signUp(Member inputMember);
}
