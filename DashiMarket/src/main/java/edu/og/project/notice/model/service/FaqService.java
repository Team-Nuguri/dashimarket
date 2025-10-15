package edu.og.project.notice.model.service;

import java.util.List;
import java.util.Map;

import edu.og.project.notice.model.dto.Faq;

public interface FaqService {
    List<Faq> selectFaqList(Map<String, Object> paramMap);
    int getFaqCount(String query);
    Faq selectFaqDetail(int faqNo);
    int insertFaq(Faq faq);
    int updateFaq(Faq faq);
    int deleteFaq(int faqNo);
    int increaseViewCount(int faqNo);
}
