package edu.og.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.member.model.dao.AjaxDAO;

@Service
public class AjaxServiceImpl implements AjaxService{
	
	@Autowired
	private AjaxDAO dao;
	
	@Override
	public int checkEmail(String email) {
		return dao.checkEmail(email);
	}
	
	@Override
	public int checkNickname(String memberNickname) {
		return dao.checkNickname(memberNickname);
	}

}
