package edu.og.project.admin.model.dto;

import java.util.Date;
import java.util.List;

import edu.og.project.common.dto.Comment;
import edu.og.project.common.dto.Image;
import edu.og.project.common.dto.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Report {
	
	private int reportNo;          // 신고 번호
	private String reportTarget;   // 신고 대상 (게시글, 댓글 등)
	private String reportTargetId;    // 신고된 대상의 번호
	private String reportReason;   // 신고 사유
	//private Date reportDate;       // 신고 일자
	private String reportDate;       // 신고 일자
	//private Date resultDate;       // 처리 완료 일자
	private String resultDate;       // 처리 완료 일자
	private int reportCode;        // 신고 유형 코드
	private int reportMemberNo;    // 신고자 회원 번호
	private int targetMemberNo;    // 신고 대상 회원 번호
	private String reportResult;   // 신고 처리 결과 (처리중, 완료 등)

}
