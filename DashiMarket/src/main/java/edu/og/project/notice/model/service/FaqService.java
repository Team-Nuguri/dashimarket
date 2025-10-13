package edu.og.project.notice.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.common.dto.Image;
import edu.og.project.notice.model.dto.Faq;

public interface FaqService {
    List<Faq> selectFaqList(Map<String, Object> paramMap);
    int getFaqCount(String query);
    Faq selectFaqDetail(int faqNo);
    int insertFaq(Faq faq);
    int insertImage(Image image);
    int updateFaq(Faq faq);
    int deleteImagesByBoardNo(String boardNo);
    int deleteFaq(int faqNo);
    
    // 조회수 증가 메서드 추가
    int increaseViewCount(int faqNo);

}
