package edu.og.project.mypage.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.og.project.common.dto.Member;
import edu.og.project.common.dto.Pagination;
import edu.og.project.common.utility.Util;
import edu.og.project.member.model.service.EmailService;
import edu.og.project.mypage.dao.MyPageMapper;

@Service
public class MyPageServiceImpl implements MyPageService {
    
    @Autowired
    private MyPageMapper mapper;
    
    @Autowired
    private BCryptPasswordEncoder bcrypt; 
    
    // 🔥 여기 수정! (folder-path → location, web-path → webpath)
    @Value("${my.profile.location}")
    private String profileLocation;
    
    @Value("${my.profile.webpath}")
    private String profileWebpath;
    
    // ⭐️ EmailService 의존성 주입
    @Autowired
    private EmailService emailService; 
    
    /**
     * 회원 정보 조회
     */
    @Override
    public Member selectMember(int memberNo) {
        return mapper.selectMember(memberNo);
    }
    
    /**
     * 프로필 수정
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProfile(Member updateMember, Member loginMember, int deleteCheck, MultipartFile profileImage) throws Exception {
        
        System.out.println("=== Service 프로필 수정 시작 ===");
        
        String profilePath = null;
        
        // 1. 이미지 삭제
        if(deleteCheck == 0) {
            profilePath = null;
            
            if(loginMember.getProfilePath() != null) {
                deleteImageFile(loginMember.getProfilePath());
            }
            System.out.println("이미지 삭제 처리");
        }
        // 2. 새 이미지 업로드
        else if(deleteCheck == 1) {
            if(profileImage != null && !profileImage.isEmpty()) {
                
                if(loginMember.getProfilePath() != null) {
                    deleteImageFile(loginMember.getProfilePath());
                }
                
                String rename = Util.fileRename(profileImage.getOriginalFilename());
                profilePath = profileWebpath + rename; // 🔥 변수명 수정
                
                // 폴더 생성
                File folder = new File(profileLocation); // 🔥 변수명 수정
                if(!folder.exists()) {
                    folder.mkdirs();
                }
                
                // 파일 저장
                profileImage.transferTo(new File(profileLocation + rename)); // 🔥 변수명 수정
                System.out.println("새 이미지 업로드: " + profilePath);
            }
        }
        // 3. 이미지 변경 없음
        else {
            profilePath = loginMember.getProfilePath();
            System.out.println("이미지 변경 없음");
        }
        
        updateMember.setProfilePath(profilePath);
        
        System.out.println("=== DB 업데이트 시작 ===");
        System.out.println("회원정보: " + updateMember);
        
        int result = mapper.updateProfile(updateMember);
        
        System.out.println("업데이트 결과: " + result + "행");
        
        return result;
    }
    
    /**
     * 이미지 파일 삭제
     */
    private void deleteImageFile(String profilePath) {
        try {
            String fileName = profilePath.substring(profilePath.lastIndexOf("/") + 1);
            String filePath = profileLocation + fileName; // 🔥 변수명 수정
            
            File file = new File(filePath);
            if(file.exists()) {
                boolean deleted = file.delete();
                System.out.println("파일 삭제: " + filePath + " - " + (deleted ? "성공" : "실패"));
            }
        } catch(Exception e) {
            System.err.println("파일 삭제 중 오류: " + e.getMessage());
        }
    }
    
    @Override
    public Member selectMemberWithPassword(int memberNo) {
        return mapper.selectMemberWithPassword(memberNo);
    }
    

    /* 회원 탈퇴
    */
   @Override
   @Transactional(rollbackFor = Exception.class)
   public int secession(String memberPw, int memberNo) {
       // 1. 로그인한 회원의 비밀번호 조회
       String encPw = mapper.selectEncPw(memberNo);
       
       // 2. 비밀번호 일치 여부 확인
       if(encPw == null) {
           System.out.println("회원 정보를 찾을 수 없습니다.");
           return 0;
       }
       
       // 3. 비밀번호 일치 시 회원 탈퇴 진행
       if(bcrypt.matches(memberPw, encPw)) {
           int result = mapper.secession(memberNo);
           System.out.println("회원 탈퇴 처리 결과: " + result);
           return result;
       }
       
       // 4. 비밀번호 불일치 시 0 반환
       System.out.println("비밀번호가 일치하지 않습니다.");
       return 0;
   }
   
   @Override
   public boolean checkPassword(int memberNo, String inputPassword) {
       // 1. DB에서 해당 회원의 암호화된 비밀번호(encPw)를 조회합니다.
       String encPw = mapper.selectEncPw(memberNo);

       // 2. 암호화된 비밀번호가 존재하고, 입력 비밀번호가 DB 비밀번호와 일치하는지 확인합니다.
       if (encPw != null) {
           // bcrypt.matches(평문, 암호화된 비밀번호)
           return bcrypt.matches(inputPassword, encPw);
       }

       // 회원 정보가 없거나, 기타 오류 시 false 반환
       return false;
   }
   
   // EmailServiceImpl 호출
   @Override
   public boolean sendSecessionEmail(Member loginMember, java.util.List<String> reasons, String detailedReason) {
       return emailService.sendSecessionReasonEmail(loginMember, reasons, detailedReason);
   }

@Override
public List<Map<String, Object>> selectGoods(Map<String, Object> paramMap) {
	
	return mapper.selectGoods(paramMap);
}

@Override
public Map<String, Object> selectGoodsWithPagination(Map<String, Object> paramMap) {
    int cp = (int) paramMap.get("cp");
    
    // 전체 게시글 수 조회
    int listCount = mapper.getGoodsListCount(paramMap);
    
    // 페이지네이션 객체 생성
    Pagination pagination = new Pagination(cp, listCount);
    
    // RowBounds 사용 (동료 스타일)
    int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
    RowBounds rowBounds = new RowBounds(offset, pagination.getLimit());
    
    // 데이터 조회 (RowBounds 사용)
    List<Map<String, Object>> goodsList = mapper.selectGoodsList(paramMap, rowBounds);
    
    // 조회 결과 return
    Map<String, Object> map = new HashMap<>();
    map.put("pagination", pagination);
    map.put("goodsList", goodsList);
    
    return map;
}

	@Override
	public int confirmPurchase(int orderItemId, int memberNo) {
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("orderItemId", orderItemId);
	    paramMap.put("memberNo", memberNo);
	    
	    return mapper.confirmPurchase(paramMap);
}

}