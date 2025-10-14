package edu.og.project.notice.model.dto;

import java.util.List;
import edu.og.project.common.dto.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Faq {
    private String faqNo;
    private String faqTitle;
    private String faqContent;
    private String faqCreateDate;
    private int readCount;
    private int boardCode;
    private String boardType;
    
    private String memberNickname;
    private int memberNo;
    
    private List<Image> imageList;
}
