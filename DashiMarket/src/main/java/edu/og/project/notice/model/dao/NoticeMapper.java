package edu.og.project.notice.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.og.project.common.dto.Image;
import edu.og.project.notice.model.dto.Notice;

@Mapper
public interface NoticeMapper {

	/** 공지사항 목록 조회 (검색 + 페이징)
     * @param paramMap
     * @return List
     */
    List<Notice> selectNoticeList(Map<String, Object> paramMap);
    
    /** 공지사항 전체 개수 조회 (검색 포함)
     * @param query
     * @return int
     */
    int getNoticeCount(@Param("query") String query);
    
    /** 공지사항 상세 조회
     * @param noticeNo
     * @return Notice
     */
    Notice selectNoticeDetail(int noticeNo);
    
    /** 공지사항 글 등록
     * @param notice
     * @return int
     */
    int insertNotice(Notice notice);
    
    /** 이미지 등록
     * @param image
     * @return
     */
    int insertImage(Image image); 
    
    /** 이전글 조회
     * @param noticeNo
     * @return Notice
     */
    Notice selectPrevNotice(int noticeNo);
    
    /** 다음글 조회
     * @param noticeNo
     * @return Notice
     */
    Notice selectNextNotice(int noticeNo);
    
    /** 공지사항 수정
     * @param notice
     * @return int
     */
    int updateNotice(Notice notice);
    
    /** 이미지 삭제
     * @param boardNo
     * @return
     */
    int deleteImagesByBoardNo(@Param("boardNo") String boardNo);  // 이미지 삭제 추가
    
    /** 공지사항 삭제 (논리 삭제)
     * @param noticeNo
     * @return int
     */
    int deleteNotice(@Param("noticeNo") int noticeNo);
    
    /** 조회수 증가
     * @param noticeNo
     * @return int
     */
    int increaseViewCount(@Param("noticeNo") int noticeNo);


}
