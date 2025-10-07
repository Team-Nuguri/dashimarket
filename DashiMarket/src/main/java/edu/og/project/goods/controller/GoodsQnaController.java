package edu.og.project.goods.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.og.project.common.dto.Comment;
import edu.og.project.goods.model.service.QnaService;

@RestController
public class GoodsQnaController {
	
	@Autowired
	QnaService service;
	
	@PostMapping("/comment/insert")
	public int qnaInsert(
			@RequestBody Comment comment
			// , 세션에서 로그인한 회원 번호 얻어오기
			
			) {
		
		comment.setMemberNo(1);
		
		System.out.println(comment);
		
		
		return service.qnaInsert(comment);
	}
	

}
