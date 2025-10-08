package edu.og.project.temp.model;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

	public Member login(Member inputMember);

	
}
