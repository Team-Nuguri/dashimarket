package edu.og.project.notice.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Faq {
    private int faqNo;
    private String faqTitle;
    private String faqContent;
    private String faqCreateDate;
    private String isDeleted; // 'N' or 'Y'
    private int boardCode; // FAQ 게시판 코드 (예: 5)
}
