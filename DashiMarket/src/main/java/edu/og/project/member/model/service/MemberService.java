package edu.og.project.member.model.service;

import edu.og.project.common.dto.Member;

public interface MemberService {

	Member login(Member inputMember);
	
	int signUp(Member inputMember);

}
