package edu.og.project.notice.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.og.project.notice.model.dto.Notice;

@Mapper
public interface NoticeMapper {

	/** 공지사항 목록 조회
	 * @param boardType
	 * @return 
	 */
	List<Notice> selectNoticeList(@Param("boardType") String boardType);
	
	
	/** 공지사항 글 등록
	 * @param notice
	 * @return
	 */
	int insertNotice(Notice notice);



}
