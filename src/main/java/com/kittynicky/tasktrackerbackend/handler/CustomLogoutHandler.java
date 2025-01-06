package com.kittynicky.tasktrackerbackend.handler;

import com.kittynicky.tasktrackerbackend.database.entity.Token;
import com.kittynicky.tasktrackerbackend.database.repository.TokenRepository;
import com.kittynicky.tasktrackerbackend.utils.Variables;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        String authHeader = request.getHeader(Variables.AUTH_HEADER_KEY);

        if (authHeader == null || !authHeader.startsWith(Variables.AUTH_HEADER_VALUE)) {
            return;
        }

        String token = authHeader.substring(Variables.AUTH_HEADER_VALUE.length());

        Token tokenEntity = tokenRepository.findByAccessToken(token).orElse(null);

        if (tokenEntity != null) {
            tokenEntity.setLoggedOut(true);
            tokenRepository.save(tokenEntity);
        }
    }
}
