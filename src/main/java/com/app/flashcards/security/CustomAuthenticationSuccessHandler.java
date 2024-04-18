package com.app.flashcards.security;

import com.app.flashcards.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String USER_ID_ATR_NAME = "userId";
    private static final String DEFAULT_SUCCESS_URL = "/flashcard-folders";
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = (User) authentication.getDetails();
        Long userId = user.getId();

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(USER_ID_ATR_NAME, userId);

        response.sendRedirect(DEFAULT_SUCCESS_URL);
    }

}
