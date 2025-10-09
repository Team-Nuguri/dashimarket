package edu.og.project.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Member {
    private int memberNo;
    private String memberEmail;
    private String memberPw;
    private String memberName;
    private String memberNickname;     
    private String memberTel;
    private String postCode;
    private String loadAddress;        
    private String detailAddress;      
    private String defaultDong;        
    private String profilePath;
    private String enrollDate;
    private String secessionFl;
    private String isAdmin;

}
