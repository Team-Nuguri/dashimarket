package edu.og.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.project.common.dto.Member;
import edu.og.project.member.model.dao.MemberDAO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDAO dao;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Override
	public Member login(Member inputMember) {
	    
	    // 🔍 입력값 검증 로그
	    log.info("============ 로그인 시도 ============");
	    log.info("입력 이메일: {}", inputMember.getMemberEmail());
	    log.info("입력 비밀번호 길이: {}", 
	        inputMember.getMemberPw() != null ? inputMember.getMemberPw().length() : "null");
	    
	    // 1. 이메일로 회원 정보 조회
	    Member loginMember = dao.login(inputMember);
	    
	    System.out.println("DB 조회 결과 (loginMember) : " + loginMember);
	    
	    if(loginMember != null) {
	        log.info("✅ 조회된 회원: {}", loginMember.getMemberEmail());
	        log.info("DB 저장된 암호화 비밀번호 앞 20자: {}", 
	            loginMember.getMemberPw().substring(0, Math.min(20, loginMember.getMemberPw().length())) + "...");
	        
	        // 🔍 비밀번호 매칭 전 상세 정보
	        String inputPw = inputMember.getMemberPw();
	        String dbPw = loginMember.getMemberPw();
	        
	        log.info("입력 비밀번호 앞 3글자: {}***", 
	            inputPw != null && inputPw.length() >= 3 ? inputPw.substring(0, 3) : inputPw);
	        log.info("DB 비밀번호가 BCrypt 형식인가? {}", dbPw.startsWith("$2a$"));
	        
	        // 2. 비밀번호 매칭 확인
	        boolean isMatch = bcrypt.matches(inputPw, dbPw);
	        log.info("🔐 비밀번호 매칭 결과: {}", isMatch);
	        
	        if(isMatch) {
	            log.info("✅ 로그인 성공: 비밀번호 일치");
	            loginMember.setMemberPw(null);
	        } else {
	            log.warn("❌ 로그인 실패: 비밀번호 불일치");
	            log.warn("   - 입력한 비밀번호를 다시 확인해주세요");
	            log.warn("   - 혹시 비밀번호를 최근에 변경하셨나요?");
	            loginMember = null;
	        }
	    } else {
	        log.warn("❌ 로그인 실패: 일치하는 이메일 없음: {}", inputMember.getMemberEmail());
	    }
	    
	    log.info("========================================");
	    return loginMember;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int signUp(Member inputMember) {
        
		// 1. 상세 주소 (DETAIL_ADDRESS)
		String detailAddress = inputMember.getDetailAddress();
		if (detailAddress != null && detailAddress.isEmpty()) {
			inputMember.setDetailAddress(" "); 
		}
		// 2. 도로명 주소 (LOAD_ADDRESS)
		String loadAddress = inputMember.getLoadAddress();
		if (loadAddress != null && loadAddress.isEmpty()) {
			inputMember.setLoadAddress(" ");
		}
		
		// 3. 우편번호 (POST_CODE)
		String postCode = inputMember.getPostCode();
		if (postCode != null && postCode.isEmpty()) {
			inputMember.setPostCode(" ");
		}
        
		// 기존 비밀번호 암호화 로직
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
		
		int result = dao.signUp(inputMember);
		
		return result;
	}
}