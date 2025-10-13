package edu.og.project.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Member implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private int memberNo;
    private String memberEmail;
    private String memberPw;
    private String memberName;
    private String memberNickname;     
    private String memberTel;
    private String postCode;
    private String loadAddress;
    private String jibunAddress;
    private String detailAddress;      
    private String defaultDong;        
    private String profilePath;    
    private String enrollDate;
    private String secessionFl;
    private String isAdmin;
    
    // 관리자 회원 조회시 필요 필드
    private String address; // 우편번호 + 도로명 + 상세주소
    private int boardCount;
    private int commentCount;

}
