package edu.og.project.member.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AjaxDAO {
	
	@Autowired
	private AjaxMapper mapper;

	public int checkEmail(String email) {
		return mapper.checkEmail(email);
	}
	
	public int checkNickname (String memberNickname) {
		return mapper.checkNickname(memberNickname);
	}
}
