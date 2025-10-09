package edu.og.project.community.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Image;
import edu.og.project.community.model.dto.Community;

@Mapper
public interface CommunityMapper {
	
	// 특정 게시판, 특정 카테고리의 삭제되지 않은 게시글 수 조회
	public int getListCount(Map<String, Object> countParam);
	
	// 커뮤니티 목록 조회
	public List<Community> selectCommunityList(Map<String, Object> param, RowBounds rowBounds);

	// 커뮤니티 상세조회
	public Community communityDetail(Map<String, Object> map);

	// 커뮤니티 댓글 조회(비동기)
	public List<Comment> selectCommentList(Map<String, Object> map);

	// 댓글 등록
	public int insertComment(Comment comment);

	// 커뮤니티 글쓰기
	public int communityWrite(Community community);

	// 이미지 업로드
	public int insertImageList(List<Image> uploadList);

	// 댓글 수정
	public int updateComment(Comment comment);

	// 댓글 삭제
	public int deleteComment(Comment comment);

	// 게시글 수정
	public int communityUpdate(Community community);
	
	// 수정시 필요 - 이미지 목록 조회
	public List<Image> selectImageList(String communityNo);

	// 이미지 삭제
	public int imageDelete(Map<String, Object> deleteMap);

	// 이미지 순서 재정렬
	public int sortImageOrder(String communityNo);

	// 특정 게시글에서 마지막 이미지 order 조회
	public int selectImageOrder(String communityNo);

	// 게시글 삭제
	public int communityDelete(String boardNo);


}
