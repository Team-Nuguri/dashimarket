package edu.og.project.goods.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Member;
import edu.og.project.goods.model.service.QnaService;

@RestController
public class GoodsQnaController {
	
	@Autowired
	QnaService service;
	
	@PostMapping("/comment/insert")
	public int qnaInsert(
			@RequestBody Comment comment,
			@SessionAttribute("loginMember") Member loginMember
			
			) {
		
		comment.setMemberNo(loginMember.getMemberNo());
		
		//System.out.println(comment);
		
		
		return service.qnaInsert(comment);
	}
	

}
