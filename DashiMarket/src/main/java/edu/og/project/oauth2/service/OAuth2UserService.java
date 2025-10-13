package edu.og.project.oauth2.service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import edu.og.project.common.dto.Member;
import edu.og.project.oauth2.entity.SocialLogin;
import edu.og.project.oauth2.entity.User;
import edu.og.project.oauth2.repository.MemberRepository;
import edu.og.project.oauth2.repository.SocialLoginRepository;
import lombok.RequiredArgsConstructor;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
									// Access Token 발급, 사용자 정보요청 , OAuth2User 생성
	private final MemberRepository userRepository;
	private final SocialLoginRepository socialLoginRepository;
	
	 private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");




	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


		OAuth2User oAuth2User = super.loadUser(userRequest);
		// 부모의 기본 기능 호출 DefaultOAuth2UserService

		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		Map<String, Object> attributes = oAuth2User.getAttributes();
		// 사용자 정보가 k v 형식으로 담김
		
		/*{
			  "id": 123456789,
			  "properties": {
			    "nickname": "김카카오",
			    "profile_image": "https://example.com/profile.jpg",
			    "thumbnail_image": "https://example.com/thumbnail.jpg"
			  },
			  "kakao_account": {
			    "email": "kakao@example.com",
			    "gender": "female",
			    "age_range": "20~29",
			    "birthday": "01-15",
			    "is_email_verified": true
			  }
			}
			*/


		// 제공자 타입 KAKAO GOOGLE
		String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
		


		// 넘겨받을 사용자 정보
		String oauthId = null; // 소셜 서비스에서 발급하는 고유 ID
		String email = null; // 사용자 이메일
		String nickname = null; // 사용자 카톡 닉네임 ex) 이름 
		String name = null; // DB에 저장할 유저 이름 카카오이메일_회원번호 형식으로 저장

		// 카카오 (KAKAO) 처리 로직
		if ("KAKAO".equals(oauthType)) {
			// 회원 카카오 고유 아이디 가져옴 ex) 4487006648 이런식
			oauthId = String.valueOf(attributes.get("id"));
			
			// 가져온 사용자 정보에서 kakao_account 부분을 따로 가져와 map에 담음
			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
			if (kakaoAccount != null) {
				// 이메일 추출
				email = (String) kakaoAccount.get("email"); 

				// profile 맵에서 닉네임 추출
				Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
				if (profile != null) {
					nickname = (String) profile.get("nickname");
					name = nickname;
					// 카카오 닉네임을 멤버 테이블 회원 이름으로
				}
			}
		} 
		
		// Optional 값이 있따면 SocialLogin 객체에 담아서 반환하고 
		// 없다면 빈 Optional을 반환함 Optional.empty()
		// 
		Optional<SocialLogin> optionalSocialLogin = socialLoginRepository.findByProviderAndSocialId(oauthType, oauthId);
		//SocialLogin Entity에서 provider, socialId 둘 다 일치하는게 있는지 찾음 
		// 존재한다 -> 기존회원-> 로그인 처리
		// 존재x -> 신규회원 -> 회원가입 처리 -> 로그인 처리
		
		// User 엔티티
		User user;

		if(optionalSocialLogin.isPresent()) { // 일치하는 회원이 존재한다면 == 기존회원이라면
			
			
			SocialLogin socialLogin = optionalSocialLogin.get();
			
			// 소셜로그인에 있는 회원 번호와 일치하는 유저 정보 찾기
			user = userRepository.findById(socialLogin.getMemberNo())
					.orElseThrow(() -> new OAuth2AuthenticationException("연결된 유저정보 db에 없음"));
			
			System.out.println("일치 회원 : " + user);

		}else {
			// 빌더
			// - 객체 생성 시, 필드 이름을 명시하며 단계적으로 값을 설정
			// 못가져오는 데이터들 임시데이터로
			user = User.builder()
					.memberEmail(email)
					.memberPw(null) // 소셜 로그인 회원은 비밀번호 x
					.memberName(email+"_"+oauthId) // 추출된 이름 또는 기본값
					.memberNickname(nickname != null ? nickname : "소셜닉네임") // 추출된 닉네임 또는 기본값
					.memberTel("010-0000-0000") // 고정 기본값
					.postCode("03189") // 고정 기본값
					.loadAddress("서울특별시 종로구 우정국로2길 21") // 고정 기본값
					.detailAddress("903호") // 고정 기본값
					.defaultDong("관철동") // 고정 기본값
					.profilePath(null)
					.enrollDate(new Date())
					.secessionFl("N")
					.isAdmin("N")
					.build();
			userRepository.save(user); 
			// save() 상황에 따라 INSERT 또는 UPDATE를 자동으로 결정
			// 새 엔티티 INSERT
			// 유저 번호는 시퀀스 번호로 자동 insert


			SocialLogin newSocialLogin = SocialLogin.builder()
					.provider(oauthType)
					.socialId(oauthId)
					.memberNo(user.getMemberNo()) // User 저장 후 회원 번호 가져와서 추가
					.build();
			socialLoginRepository.save(newSocialLogin);
			
			System.out.println("회원가입 성공");
		}

		System.out.println(oauthType);
		System.out.println(oauthId);
		System.out.println(user);
		
		
		Member loginMember = convertUserToMember(user);



		return  new CustomOAuth2User(
				loginMember,
				attributes, // 원본 소셜 속성
				userNameAttributeName // 소셜 ID를 가져올 키 (카카오의 경우 'id')
		);
		
		
		
		
	}
	
    private Member convertUserToMember(User user) {
    	// common/dto/member
    	// member dto 에 user 엔티티에서 값 가져와 세팅 후 리턴
        Member member = new Member();
        
        // Entity에서 DTO로 필드 매핑
        if (user.getMemberNo() != null) {
            member.setMemberNo(user.getMemberNo().intValue());
        }
        member.setMemberEmail(user.getMemberEmail());
        member.setMemberPw(user.getMemberPw()); // null일 수 있음
        member.setMemberName(user.getMemberName());
        member.setMemberNickname(user.getMemberNickname());
        member.setMemberTel(user.getMemberTel());
        member.setPostCode(user.getPostCode());
        member.setLoadAddress(user.getLoadAddress());
        member.setDetailAddress(user.getDetailAddress());
        member.setDefaultDong(user.getDefaultDong());
        member.setProfilePath(user.getProfilePath());
        
        
        if (user.getEnrollDate() != null) {
            member.setEnrollDate(DATE_FORMAT.format(user.getEnrollDate()));
       } else {
            member.setEnrollDate(null);
       }
       
       member.setSecessionFl(user.getSecessionFl());
       member.setIsAdmin(user.getIsAdmin());
        
     
        
        return member;
    }







}
