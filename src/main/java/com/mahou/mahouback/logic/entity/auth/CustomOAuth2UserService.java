package com.mahou.mahouback.logic.entity.auth;

import com.mahou.mahouback.logic.entity.role.RoleEnum;
import com.mahou.mahouback.logic.entity.role.RoleRepository;
import com.mahou.mahouback.logic.entity.user.User;
import com.mahou.mahouback.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        Optional<User> userOptional = userRepository.findByEmail(email);

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setName(oAuth2User.getAttribute("given_name"));
            user.setLastname(oAuth2User.getAttribute("family_name"));
            user.setPhoto(oAuth2User.getAttribute("picture"));
            user.setStatus(true);
            user.setPassword("");
            user.setRole(roleRepository.findByName(RoleEnum.USER).orElseThrow());
            userRepository.save(user);
        }

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())),
                oAuth2User.getAttributes(),
                "email"
        );
    }

}
