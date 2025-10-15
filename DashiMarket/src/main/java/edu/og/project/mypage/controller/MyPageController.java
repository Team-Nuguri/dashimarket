package edu.og.project.mypage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.og.project.common.dto.Member;
import edu.og.project.mypage.model.MyPageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/myPage")
@SessionAttributes("loginMember")
public class MyPageController {
    
    @Autowired
    private MyPageService service;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /**
     * 프로필 관리 페이지
     */
    @GetMapping("/profile")
    public String myPageProfile(
        @SessionAttribute(value = "loginMember", required = false) Member loginMember, 
        Model model) {  	

        
        if (loginMember == null) {
            return "redirect:/member/login"; 
        }
        
        // DB에서 최신 회원 정보 조회
        Member memberInfo = service.selectMember(loginMember.getMemberNo());
        
        if(memberInfo == null) {
            model.addAttribute("message", "회원 정보를 불러오는데 실패했습니다.");
            return "redirect:/";
        }
        
        model.addAttribute("loginMember", memberInfo);        
    	
    	
        return "myPage/myPage-profile";
    }
    
    /**
     * 현재 비밀번호 확인 (AJAX)
     */
    @PostMapping("/checkPassword")
    @ResponseBody
    public Map<String, Object> checkPassword(
            @RequestBody Map<String, String> paramMap,
            @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 로그인 체크
        if (loginMember == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }
        
        String inputPassword = paramMap.get("password");
        
        // DB에서 최신 회원 정보 조회 (비밀번호 포함)
        Member memberInfo = service.selectMemberWithPassword(loginMember.getMemberNo());
        
        if(memberInfo != null && passwordEncoder.matches(inputPassword, memberInfo.getMemberPw())) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        
        return result;
    }
    
    /**
     * 프로필 정보 수정
     */
    @PostMapping("/profile")
    public String updateProfile(
            Member updateMember,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "deleteCheck", defaultValue = "-1") int deleteCheck,
            @RequestParam(value = "newPwd", required = false) String newPwd,
            @SessionAttribute(value = "loginMember", required = false) Member loginMember,
            RedirectAttributes ra,
            Model model) {
        
        // 로그인 체크
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }
        
        String message = null;
        
        try {
            updateMember.setMemberNo(loginMember.getMemberNo());
            
            // 비밀번호 변경이 있으면 암호화
            if(newPwd != null && !newPwd.trim().isEmpty()) {
                String encryptedPw = passwordEncoder.encode(newPwd);
                updateMember.setMemberPw(encryptedPw);
            }
            
            int result = service.updateProfile(updateMember, loginMember, deleteCheck, profileImage);
            
            if(result > 0) {
                message = "회원 정보가 성공적으로 수정되었습니다.";
                
                // DB에서 최신 정보 다시 조회
                Member updatedMember = service.selectMember(loginMember.getMemberNo());
                
                if(updatedMember != null) {
                    model.addAttribute("loginMember", updatedMember);
                }
            } else {
                message = "회원 정보 수정에 실패했습니다.";
            }
        
        } catch (Exception e) {
            e.printStackTrace();
            message = "회원 정보 수정 중 오류가 발생했습니다: " + e.getMessage();
        }
        
        ra.addFlashAttribute("message", message);
        return "redirect:/myPage/profile";
    }
    
    /**
     * 회원탈퇴 페이지 이동
     */
    @GetMapping("/secession")
    public String secession(
        @SessionAttribute(value = "loginMember", required = false) Member loginMember, 
        RedirectAttributes ra) {
        
        // 로그인 상태 체크
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요한 서비스입니다.");
            return "redirect:/member/login";
        }
        
        return "myPage/myPage-secession";
    }
    
    /**
     * AJAX: 회원 탈퇴 전 비밀번호 확인 및 탈퇴 사유 처리 (선택사항)
     */
    @PostMapping("/secessionCheckAndSendEmail")
    @ResponseBody
    public Map<String, Object> secessionCheckAndSendEmail(
            @RequestBody Map<String, Object> paramMap,
            @SessionAttribute(value = "loginMember", required = false) Member loginMember) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 로그인 체크
        if (loginMember == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }
        
        String inputPassword = (String) paramMap.get("memberPw");
        
        // 비밀번호 확인
        Member memberInfo = service.selectMemberWithPassword(loginMember.getMemberNo());
        boolean isPasswordMatch = memberInfo != null && 
                                 passwordEncoder.matches(inputPassword, memberInfo.getMemberPw());
        
        if (!isPasswordMatch) {
            result.put("success", false);
            result.put("message", "현재 비밀번호가 일치하지 않습니다.");
            return result;
        }
        
        // 탈퇴 사유 처리 (선택사항)
        try {
            @SuppressWarnings("unchecked")
            java.util.List<String> reasons = (java.util.List<String>) paramMap.get("reasons");
            String detailedReason = (String) paramMap.get("detailedReason");
            
            // 이메일 전송이 필요하다면 여기서 처리
            // boolean emailSendResult = service.sendSecessionEmail(loginMember, reasons, detailedReason);
            
            result.put("success", true);
            result.put("message", "비밀번호가 확인되었습니다.");
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "탈퇴 사유 처리 중 오류가 발생했습니다.");
        }
        
        return result;
    }

    /**
     * 회원탈퇴 처리
     */
    @PostMapping("/secession")
    public String secession(
            @RequestParam("memberPw") String memberPw,
            @SessionAttribute(value = "loginMember", required = false) Member loginMember,
            SessionStatus status,
            RedirectAttributes ra,
            HttpServletResponse resp) {
        
        // 로그인 체크
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }
        
        // 1. 로그인한 회원의 회원 번호 얻어오기
        int memberNo = loginMember.getMemberNo();
        
        // 2. 회원 탈퇴 서비스 호출 (DB 플래그 변경)
        int result = service.secession(memberPw, memberNo); 
        
        String message = null;
        String path = "redirect:";
        
        if(result != 0) {
            // 3. 탈퇴 성공 시 (DB 업데이트 성공)
            message = "탈퇴 되었습니다.";
            status.setComplete();
            path += "/";
            
            // 쿠키 삭제
            Cookie cookie = new Cookie("saveId", ""); 
            cookie.setMaxAge(0);
            cookie.setPath("/"); 
            resp.addCookie(cookie); 
            
        } else {
            // 4. 탈퇴 실패 시 (비밀번호 불일치 또는 DB 오류)
            message = "현재 비밀번호가 일치하지 않습니다.";
            path += "secession";    
        }
        
        ra.addFlashAttribute("message", message);
        return path;
    }
    
	@GetMapping("/fraud")
	public String fraud() { 
	    return "myPage/fraudService";
	}
	
	
	@GetMapping("/goods")	
	public String selectGoods (
	        @SessionAttribute("loginMember") Member loginMember,
	        @RequestParam(value = "cp", defaultValue = "1") int cp,
	        @RequestParam(value = "keyword", required = false) String keyword,
	        Model model) {
	    
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("memberNo", loginMember.getMemberNo());
	    paramMap.put("keyword", keyword);
	    paramMap.put("cp", cp);
	    
	    // 페이지네이션과 함께 데이터 조회
	    Map<String, Object> resultMap = service.selectGoodsWithPagination(paramMap);
	    
	    model.addAttribute("goodsList", resultMap.get("goodsList"));
	    model.addAttribute("pagination", resultMap.get("pagination"));
	    model.addAttribute("keyword", keyword);
	    
	    return "myPage/myPage-goods";		
	}
	
	
	@GetMapping("/api/goods/list") // 비동기
	@ResponseBody
	public Map<String, Object> selectGoodsApi(
	        @SessionAttribute("loginMember") Member loginMember,
	        @RequestParam(value = "cp", defaultValue = "1") int cp,
	        @RequestParam(value = "keyword", required = false) String keyword) {
	    
	    // DB에 전달할 Map 생성 및 데이터 담기
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("memberNo", loginMember.getMemberNo());
	    paramMap.put("keyword", keyword);
	    paramMap.put("cp", cp); //
	    
	    // 페이지네이션 포함된 결과 반환
	    return service.selectGoodsWithPagination(paramMap);
	}
	
	@PostMapping("/api/goods/confirm")
	@ResponseBody
	public Map<String, Object> confirmPurchase(
	        @RequestParam("orderItemId") int orderItemId,
	        @SessionAttribute("loginMember") Member loginMember) {
	    
	    Map<String, Object> result = new HashMap<>();
	    System.out.println("구매확정input" + orderItemId);
	    try {
	        // 구매 확정 처리 (SHIPPING 테이블의 DELIVERY_STATUS 업데이트)
	        int updateResult = service.confirmPurchase(orderItemId, loginMember.getMemberNo());
	        
	        if (updateResult > 0) {
	            result.put("success", true);
	            result.put("message", "구매가 확정되었습니다.");
	        } else {
	            result.put("success", false);
	            result.put("message", "구매 확정에 실패했습니다.");
	        }
	    } catch (Exception e) {
	        result.put("success", false);
	        result.put("message", "오류가 발생했습니다: " + e.getMessage());
	    }
	    
	    System.out.println("구매확정" + result);
	    
	    return result;
	}

}

