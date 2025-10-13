package edu.og.project.notice.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.project.common.dto.Image;
import edu.og.project.notice.model.dao.FaqMapper;
import edu.og.project.notice.model.dto.Faq;

@Service
public class FaqServiceImpl implements FaqService {
	
	@Autowired
	private FaqMapper mapper;
	
	@Override
	public List<Faq> selectFaqList(Map<String, Object> paramMap) {
		return mapper.selectFaqList(paramMap);
	}
	
	@Override
	public int getFaqCount(String query) {
		return mapper.getFaqCount(query);
	}
	
	@Override
	public Faq selectFaqDetail(int faqNo) {
		return mapper.selectFaqDetail(faqNo);
	}
	
	@Transactional
	@Override
	public int insertFaq(Faq faq) {
		return mapper.insertFaq(faq);
	}
	
	@Transactional
	@Override
	public int insertImage(Image image) {
		return mapper.insertImage(image);
	}
	
	@Transactional
	@Override
	public int updateFaq(Faq faq) {
		return mapper.updateFaq(faq);
	}
	
	@Transactional
	@Override
	public int deleteImagesByBoardNo(String boardNo) {
		return mapper.deleteImagesByBoardNo(boardNo);
	}
	
	@Transactional
	@Override
	public int deleteFaq(int faqNo) {
		return mapper.deleteFaq(faqNo);
	}
	
	@Transactional
	@Override
	public int increaseViewCount(int faqNo) {
	    return mapper.increaseViewCount(faqNo);
	}

}
