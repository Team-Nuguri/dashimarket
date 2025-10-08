package edu.og.project.goods.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.common.dto.Comment;
import edu.og.project.goods.model.dao.QnaMapper;

@Service
public class QnaServiceImpl implements QnaService {
	
	@Autowired
	QnaMapper mapper;
	
	
	// qna 등록
	@Override
	public int qnaInsert(Comment comment) {
		
		int result = 0;
		
		if(comment.getParentCommentNo() == 0) {
			result = mapper.qnaInsert(comment);
		}else {
			
			comment.setIsSecret(mapper.selectIsSecret(comment.getParentCommentNo()));
			
			result = mapper.qnaInsert(comment);
			
			if(result == 1) {
				
				result = mapper.statusUpdate(comment);
			}
		}
		
		
		return result;
	}

}
