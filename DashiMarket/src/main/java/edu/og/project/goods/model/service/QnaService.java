package edu.og.project.goods.model.service;

import java.util.Map;

import edu.og.project.common.dto.Comment;

public interface QnaService {
	
	
	
	/** qna 등록
	 * @param comment
	 * @return int 성공한 행 개수
	 */
	int qnaInsert(Comment comment);
	
}
