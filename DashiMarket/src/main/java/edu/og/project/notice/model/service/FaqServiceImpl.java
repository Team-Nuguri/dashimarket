package edu.og.project.notice.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.og.project.notice.model.dao.FaqMapper;

@Service
public class FaqServiceImpl implements FaqService{
	
	@Autowired
	private FaqMapper mapper;
}
