package edu.og.project.oauth2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Entity
@Table(name = "MEMBER") 
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

	// 회원 번호 (PK, NUMBER)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MEMBER_NO")
	@SequenceGenerator(name="SEQ_MEMBER_NO", sequenceName = "SEQ_MEMBER_NO", allocationSize = 1)
	@Column(name = "MEMBER_NO", nullable = false)
	private Long memberNo;

	@Column(name = "MEMBER_EMAIL", nullable = false, length = 50)
	private String memberEmail;

	@Column(name = "MEMBER_PW", length = 300)
	private String memberPw;

	// 이름 (VARCHAR2(30), Not Null)
	@Column(name = "MEMBER_NAME", nullable = false, length = 30)
	private String memberName;

	// 닉네임 (VARCHAR2(30), Not Null). 유니크 제약조건은 DB 레벨에서 필요
	@Column(name = "MEMBER_NICKNAME", nullable = false, length = 30)
	private String memberNickname;

	// 전화번호 (VARCHAR2(13), Not Null - 스키마 기준)
	@Column(name = "MEMBER_TEL", nullable = false, length = 13)
	private String memberTel;

	// 우편번호 (VARCHAR2(6), Not Null - 스키마 기준)
	@Column(name = "POST_CODE", nullable = false, length = 6)
	private String postCode;

	// 도로명 주소 (VARCHAR2(50), Not Null - 스키마 기준)
	@Column(name = "LOAD_ADDRESS", nullable = false, length = 50)
	private String loadAddress;

	// 상세 주소 (VARCHAR2(50), Not Null - 스키마 기준)
	@Column(name = "DETAIL_ADDRESS", nullable = false, length = 50)
	private String detailAddress;

	// 동/읍/면 (VARCHAR2(30), Not Null - 스키마 기준)
	@Column(name = "DEFAULT_DONG", nullable = false, length = 30)
	private String defaultDong;

	// 프로필 이미지 경로 (VARCHAR2(100), Nullable)
	@Column(name = "PROFILE_PATH", length = 100)
	private String profilePath;

	// 가입일 (DATE, Not Null, 기본값 SYSDATE)
	@Temporal(TemporalType.DATE)
	@Column(name = "ENROLL_DATE", nullable = false)
	private Date enrollDate; 

	// 탈퇴 여부 (CHAR(1), Not Null, 기본값 'N')
	@Column(name = "SECESSIONFL", nullable = false, length = 1)
	private String secessionFl;

	// 관리자 여부 (CHAR(1), Not Null, 기본값 'N')
	@Column(name = "IS_ADMIN", nullable = false, length = 1)
	private String isAdmin;
	
	// 지번 주소
	@Column(name = "JIBUN_ADDRESS", length = 200 )
	private String jibunAddress;

	/**
	 * 회원 정보 업데이트 (소셜 로그인 시 이미 존재하는 회원 정보 갱신에 사용)
	 * 이메일, 이름, 닉네임만 갱신 가능하도록 설정
	 * @param memberName 갱신할 이름
	 * @param memberNickname 갱신할 닉네임
	 */
	public void updateMemberInfo(String memberName, String memberNickname) {
		this.memberName = memberName;
		this.memberNickname = memberNickname;
	}

	// JPA persist 전에 필드 기본값을 설정하는 콜백
	@jakarta.persistence.PrePersist
	protected void onCreate() {
		if (enrollDate == null) {
			enrollDate = new Date();
		}
		if (secessionFl == null) {
			secessionFl = "N";
		}
		if (isAdmin == null) {
			isAdmin = "N";
		}
		
        if (memberTel == null) {
            memberTel = "010-0000-0000";
        }
        if (postCode == null) {
            postCode = "03189"; // 유효한 6자리 우편번호 형식
        }
        if (loadAddress == null) {
            loadAddress = "서울특별시 종로구 우정국로2길 21";
        }
        if (detailAddress == null) {
            detailAddress = "903호";
        }
        if (defaultDong == null) {
            defaultDong = "관철동";
        }
        if (jibunAddress == null) {
        	jibunAddress = "서울 종로구 관철동 45-1";
        }
	}
}
