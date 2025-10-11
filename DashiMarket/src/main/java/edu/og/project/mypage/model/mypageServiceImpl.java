package edu.og.project.mypage.model;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.og.project.common.dto.Member;
import edu.og.project.common.utility.Util;
import edu.og.project.mypage.dao.mypageMapper;

@Service
@PropertySource("classpath:/config.properties") 
public class mypageServiceImpl implements mypageService { 
    // 💡 클래스명 오타 수정: mypageSeviceImpl -> mypageServiceImpl

	@Value("${my.member.webpath}")
	private String webPath; // 예: /images/member/
	
	@Value("${my.member.location}")
	private String filePath; // 예: /home/project/uploadImages/member/

	@Autowired
	private mypageMapper mapper;
	
    // 💡 기존 updateInfo 메서드는 제거하거나, 아래 updateProfile 메서드로 통합하여 사용합니다.

    /** 회원 정보 및 프로필 이미지 수정 */
	@Transactional(rollbackFor = Exception.class) // 파일 I/O 실패 시 롤백
	@Override
	public int updateProfile(Member updateMember, Member loginMember, 
                             int deleteCheck, MultipartFile profileImage) throws IllegalStateException, IOException {
        
        String updatePath = null; 
        String originalPath = loginMember.getProfilePath(); // 기존 이미지 경로
        
        try {
            // 1. 이미지 변경/삭제 상태에 따른 경로 처리
            if (deleteCheck == 1) { // 1: 새 이미지로 변경
                // 파일명 변경 및 서버 저장
                String fileName = Util.fileRename(profileImage.getOriginalFilename()); 
                File saveFile = new File(filePath + fileName);
                profileImage.transferTo(saveFile); 
                
                updatePath = webPath + fileName; 

            } else if (deleteCheck == 0) { // 0: 이미지 삭제
                updatePath = null; // DB의 PROFILE_PATH를 NULL로 설정

            } else { // -1: 변경/삭제 없음
                updatePath = originalPath; // 기존 경로 유지
            }

            // 2. 이전 이미지 파일 삭제 (변경되거나 삭제되었고, 기본 이미지가 아닌 경우)
            // loginMember가 프로필 경로를 가지고 있고, 변경/삭제 상태인 경우
            if (deleteCheck != -1 && originalPath != null && !originalPath.contains("user.png")) { 
                // DB에 저장된 웹 경로를 실제 서버 경로로 변환하여 파일 삭제
                String deleteFilePath = originalPath.substring(webPath.length());
                File deleteFile = new File(filePath + deleteFilePath);
                
                if (deleteFile.exists()) {
                    deleteFile.delete();
                }
            }

            // 3. updateMember 객체에 최종 프로필 경로 설정
            updateMember.setProfilePath(updatePath);
            
            // 4. 회원 정보 및 이미지 경로 DB 업데이트 (Mapper 실행)
            int result = mapper.updateProfile(updateMember);

            // 5. DB 업데이트 성공 시 세션 정보 갱신을 위해 loginMember에 최종 경로 반영
            if(result > 0) {
                loginMember.setProfilePath(updatePath); 
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            // 파일 처리 오류 발생 시 롤백을 위해 예외를 다시 던집니다.
            throw e; 
        }
	}
}