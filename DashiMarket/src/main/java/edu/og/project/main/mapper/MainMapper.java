package edu.og.project.main.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {

	// 인트로 검색
	List<Map<String, Object>> introSearch(String query);

}
