package com.app.flashcards.security;

import com.app.flashcards.entity.User;
import com.app.flashcards.enums.SessionAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String DEFAULT_SUCCESS_URL = "/folders";
    private final UserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SessionAttributes.USER_ID.getName(), user.getId());

        response.sendRedirect(DEFAULT_SUCCESS_URL);
    }

}
