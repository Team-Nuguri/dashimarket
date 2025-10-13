package edu.og.project.main.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.main.mapper.MainMapper;

@Service
public class MainServiceImpl implements MainService{
	
	@Autowired
	private MainMapper mapper;

	// 인트로 검색
	@Override
	public List<Map<String, Object>> introSearch(String query) {
		return mapper.introSearch(query);
	}

}
