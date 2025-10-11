package edu.og.project.mypage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.common.dto.Member;
import edu.og.project.mypage.model.mypageService;

@Controller
@RequestMapping("/myPage")
public class mypageController {
	
	@Autowired
	private mypageService service;
	
	@GetMapping("/profile")
	public String mypageProfile (@SessionAttribute("loginMember") Member loginMember, 
			Model model) {
		model.addAttribute("loginMember", loginMember);
		
		return "myPage/myPage-profile";
	}
		
	@GetMapping("/fraud")
	public String fraudService () {
		
		return "myPage/fraudService";
	}

	
	// 프로필 이미지 + 회원 정보 수정
	@PostMapping("/profile")
	public String updateProfile(
					Member updateMember, // 닉네임, 전화번호, 주소 등 폼에서 전송된 정보
					@RequestParam("profileImage") MultipartFile profileImage, // 업로드 파일
	                @RequestParam("deleteCheck") int deleteCheck, // 이미지 변경/삭제 상태
					@SessionAttribute("loginMember") Member loginMember, // 현재 로그인한 회원 세션
					RedirectAttributes ra // 리다이렉트 시 메세지 전달
					) {
	        
	        String message = null;
	        String path = "redirect:/myPage/profile";
	        
	        try {
	            // updateMember에 회원 번호 설정
	            updateMember.setMemberNo(loginMember.getMemberNo());

	            // 서비스 호출 (회원 정보 + 이미지 처리)
	            int result = service.updateProfile(updateMember, loginMember, deleteCheck, profileImage);
	            
	            if(result > 0) {
	                message = "회원 정보가 성공적으로 수정되었습니다.";
	                
	                // 세션에 저장된 로그인 회원 정보 업데이트 (중요!)
	                loginMember.setMemberNickname(updateMember.getMemberNickname());
	                loginMember.setMemberTel(updateMember.getMemberTel());
	                loginMember.setPostCode(updateMember.getPostCode());
	                loginMember.setLoadAddress(updateMember.getLoadAddress());
	                loginMember.setDetailAddress(updateMember.getDetailAddress());
	                loginMember.setDefaultDong(updateMember.getDefaultDong());
	                // loginMember.setProfilePath()는 ServiceImpl에서 이미 갱신되었습니다.

	            } else {
	                message = "회원 정보 수정에 실패했습니다. (수정된 내용 없음 또는 DB 오류)";
	            }
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	            message = "회원 정보 수정 중 오류가 발생했습니다.";
	            // 파일 업로드 오류 등 발생 시, 에러 페이지로 리다이렉트하거나 오류 메시지를 더 명확히 할 수 있습니다.
	        }
	        
	        ra.addFlashAttribute("message", message);
				
		    return path;
		}
			
}
