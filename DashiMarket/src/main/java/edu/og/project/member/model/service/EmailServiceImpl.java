package edu.og.project.member.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
