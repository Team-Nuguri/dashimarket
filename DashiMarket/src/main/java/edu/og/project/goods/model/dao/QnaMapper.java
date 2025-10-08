package edu.og.project.goods.model.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.common.dto.Comment;

@Mapper
public interface QnaMapper {
	
	
	// qna 등록
	int qnaInsert(Comment comment);
	
	// 부모 댓글 비밀글 여부 조회
	String selectIsSecret(int parentCommentNo);
	
	// 부모 댓글 답변 상태 변경
	int statusUpdate(Comment comment);

}
