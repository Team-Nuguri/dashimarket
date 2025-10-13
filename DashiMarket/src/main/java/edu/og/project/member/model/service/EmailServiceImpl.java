package edu.og.project.member.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.project.common.dto.Member;
import edu.og.project.member.model.dao.EmailDAO;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private EmailDAO dao;
	
	@Autowired
	private JavaMailSender mailSender;
	
	private String fromEmail = "every3322@gmail.com";
	private String fromUsername = "수업용프로젝트";
	
	@Override
	public String createAuthKey() {
		
		String key = "";
		for(int i=0; i<6; i++) {
			
			int sell = (int)(Math.random()*3);
			
			if(sell == 0) {
				int num = (int)(Math.random()*10);
				key += num;
			}else {
				char ch = (char)(Math.random()*26+65);
				int sel2 = (int) (Math.random()*2);
				if(sel2 == 0) {
					ch = (char)(ch + ('a' - 'A'));
				}
				key += ch;
			}
		}
		return key;
	}
	
	@Transactional
	@Override
	public int signUp(String email, String title) {
		
		String authKey = createAuthKey();
		try {
			MimeMessage mail = mailSender.createMimeMessage();
			
			String subject = "[Dashimarket]" + title + "인증코드";
			
			String charset = "UTF-8";
			
			String mailContent
				= "<p>Dashimarket" + title + "인증코드입니다.</p>"
				+ "<h3 style='color:blue'>" + authKey + "</h3>";
			
			mail.setFrom(new InternetAddress(fromEmail, fromUsername));
			
			mail.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			
			mail.setSubject(subject, charset);
			
			mail.setText(mailContent, charset, "html");
			
			mailSender.send(mail);
				
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("authKey", authKey);
		map.put("email", email);
		
		int result = dao.updateAuthKey(map);
		
		if(result == 0) {
			result = dao.insertAuthKey(map);
		}
		
		return result;
	}

	@Override
	public int checkAuthKey (Map<String, Object> paramMap) {
		return dao.checkAuthKey(paramMap);
	}

	 /** 구현: 회원 탈퇴 사유 관리자에게 전송 */
    @Override
    public boolean sendSecessionReasonEmail(Member loginMember, List<String> reasons, String detailedReason) {
        
        // 이메일을 수신할 운영팀 관리자 주소로 반드시 변경해 주세요.
        String toEmail = "eric2000@kotis.net"; 
        
        // application.properties에 설정된 username과 동일해야 합니다.
        // 현재 설정: every3322@gmail.com
        String fromEmail = "every3322@gmail.com";  

        // 이메일 내용 구성 (HTML 형식)
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<h2>[탈퇴 사유] 회원 탈퇴 사유가 접수되었습니다.</h2>");
        emailContent.append("<p><strong>회원 번호:</strong> ").append(loginMember.getMemberNo()).append("</p>");
        emailContent.append("<p><strong>이메일:</strong> ").append(loginMember.getMemberEmail()).append("</p>");
        emailContent.append("<p><strong>닉네임:</strong> ").append(loginMember.getMemberNickname()).append("</p>");
        emailContent.append("<hr>");
        
        emailContent.append("<h3> 선택된 탈퇴 사유</h3>");
        if (reasons != null && !reasons.isEmpty()) {
            emailContent.append("<ul>");
            for (String reason : reasons) {
                emailContent.append("<li>").append(reason).append("</li>"); 
            }
            emailContent.append("</ul>");
        } else {
            emailContent.append("<p>선택된 사유 없음</p>");
        }
        
        emailContent.append("<h3> 상세 사유</h3>");
        if (detailedReason != null && !detailedReason.trim().isEmpty()) {
            emailContent.append("<pre style='background-color: #f7f7f7; padding: 15px; border-radius: 5px;'>")
                        .append(detailedReason)
                        .append("</pre>");
        } else {
            emailContent.append("<p>상세 사유 미작성</p>");
        }
        emailContent.append("<br><p>이 정보는 서비스 개선을 위해 활용됩니다.</p>");


        try {
            MimeMessage message = mailSender.createMimeMessage();
            // true: 멀티파트 메시지 사용 (HTML, 첨부파일 등)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); 
            
            helper.setFrom(fromEmail); 
            helper.setTo(toEmail);
            helper.setSubject("[필수 확인] " + loginMember.getMemberNickname() + " 회원의 탈퇴 사유 접수");
            // 두 번째 인자 true: HTML 형식으로 전송
            helper.setText(emailContent.toString(), true); 
            
            mailSender.send(message);
            System.out.println("탈퇴 사유 이메일 전송 성공: " + toEmail);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("탈퇴 사유 이메일 전송 실패: " + e.getMessage());
            return false;
        }
    }
}
