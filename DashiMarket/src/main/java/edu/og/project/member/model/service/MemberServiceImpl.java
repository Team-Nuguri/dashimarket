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
    
    // 1. 이메일로 회원 정보 조회
    Member loginMember = dao.login(inputMember);
    
    System.out.println("DB 조회 결과 (loginMember) : " + loginMember);
    
    if(loginMember != null) {
        log.info("조회된 회원: {}", loginMember.getMemberEmail()); // 아이디 조회 성공 로그
        
        // 2. 비밀번호 매칭 확인
        if(bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())){
            log.info("로그인 성공: 비밀번호 일치"); // 비밀번호 일치 로그
            loginMember.setMemberPw(null);
        }else {
            log.warn("로그인 실패: 비밀번호 불일치"); // 비밀번호 불일치 로그
            loginMember = null;
        }
    } else {
        log.warn("로그인 실패: 일치하는 아이디 없음: {}", inputMember.getMemberEmail()); // 아이디 조회 실패 로그
    }
    
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