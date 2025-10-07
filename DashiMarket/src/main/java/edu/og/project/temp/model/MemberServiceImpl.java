package edu.og.project.temp.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Override
	public Member login(Member inputMember) {
		
		Member loginMember = mapper.login(inputMember);
		
		if(loginMember != null) {
			
			if(bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
				loginMember.setMemberPw(null);
			}else {
				loginMember = null;
			}
		}
		
		return loginMember;
	}

}
