package edu.og.project.notice.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.og.project.notice.model.dto.Faq;

@Mapper
public interface FaqMapper {
    List<Faq> selectFaqList(Map<String, Object> paramMap);
    int getFaqCount(@Param("query") String query);
    Faq selectFaqDetail(int faqNo);
    int insertFaq(Faq faq);
    int updateFaq(Faq faq);
    int deleteFaq(@Param("faqNo") int faqNo);
    int increaseViewCount(@Param("faqNo") int faqNo);
}
