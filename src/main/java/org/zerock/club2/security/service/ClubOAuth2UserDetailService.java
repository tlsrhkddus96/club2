package org.zerock.club2.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.club2.entity.ClubMember;
import org.zerock.club2.entity.ClubMemberRole;
import org.zerock.club2.repository.ClubMemberRepository;
import org.zerock.club2.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuth2UserDetailService extends DefaultOAuth2UserService {

    private final ClubMemberRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        log.info("==================================");
        log.info("userRequest : " + userRequest);   //org.springframework.security.oauth2.client.userinfo.Oauth2UserRequest객체
        

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName : " + clientName); //Google로 출력
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("======================================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k + ":" + v);  //sub, picture, email, email_verified, EMAIL 등이 출력
        });

        String email = null;

        if(clientName.equals("Google")){    // 구글을 이용하는 경우

            email = oAuth2User.getAttribute("email");
        }

        log.info("EMAIL : " + email);

       // ClubMember member = saveSocialMember(email);

       // return oAuth2User;

        ClubMember member = saveSocialMember(email);

        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name())
                ).collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        clubAuthMember.setName(member.getName());

        return clubAuthMember;

    }

    private ClubMember saveSocialMember(String email){

        //기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<ClubMember> result = repository.findByEmail(email, true);

        if(result.isPresent()){
            return result.get();
        }

        //없다면 회원 추가 패스워드는 1111 이름은 그냥 이메일 주소로
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        clubMember.addMemberRole(ClubMemberRole.USER);

        repository.save(clubMember);

        return clubMember;

    }

}
