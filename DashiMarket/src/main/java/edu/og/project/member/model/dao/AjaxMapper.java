package edu.og.project.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AjaxMapper {
	
	int checkEmail(String email);
	
	int checkNickname (String memberNickname);

}
