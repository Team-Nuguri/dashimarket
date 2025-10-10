package edu.og.project.notice.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.og.project.notice.model.dto.Notice;

@Mapper
public interface NoticeMapper {

	/**
	 * @param boardType
	 * @return 
	 */
	List<Notice> selectNoticeList(@Param("boardType") String boardType);



}
