package com.mahou.mahouback.logic.entity.auth;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, java.io.IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserDetails email = oAuth2User.getAttribute("email");

        String token = jwtService.generateToken(email);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
    }
}