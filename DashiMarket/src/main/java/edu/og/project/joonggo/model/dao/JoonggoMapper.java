package edu.og.project.joonggo.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.og.project.common.dto.Image;
import edu.og.project.joonggo.model.dto.Joonggo;
import edu.og.project.joonggo.model.dto.JoonggoWrite;
import edu.og.project.joonggo.model.dto.SimilarItem;

@Mapper
public interface JoonggoMapper {
	
	
	// 중고 상품 상세 조회
	Joonggo selectJoonggoDetail(String joonggoNo);
	
	
	// 비슷한 상품 목록 조회
	List<SimilarItem> selectJonggoList(Map<String, Object> map);

	
	// 중고 상품 삭제
	int deleteJoonggoItem(String joonggoNo);

	
	// 중고 상품 정보 삽입
	int joonggoInsert(JoonggoWrite joonggoWrite);

	
	// 가격 삽입
	int joonggoPriceInsert(JoonggoWrite joonggoWrite);
	
	
	
	// 이미지 삽입 
	int insertImage(List<Image> uploadImage);

	
	// 보드 내용 수정
	int joonggoUpdate(JoonggoWrite joonggoWrite);
	
	
	// 상푸 ㅁ가격 수정
	int joonggoPriceUpdate(JoonggoWrite joonggoWrite);

	
	
	// 이미지 삭제
	int imageDelete(Map<String, Object> deleteMap);

	
	// 이미지 순서 재정렬
	int sortImageOrder(String joonggoNo);

	
	// 시작 인덱스 조회
	int selectImageOrder(String joonggoNo);

	
	// 좋아요 삽입
	int likeInsert(Map<String, Object> paramMap);

	
	// 좋아요 삭제
	int deleteInsert(Map<String, Object> paramMap);

	
	// 좋아요 개수 확인
	int countLike(Object object);

	
	// 좋아요 확인
	int likeSelect(Map<String, Object> map);

	
	// 조회수 증가
	int updateReadCount(String joonggoNo);
	
	

}
