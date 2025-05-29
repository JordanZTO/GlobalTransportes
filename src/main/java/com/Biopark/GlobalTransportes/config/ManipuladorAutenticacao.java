package com.Biopark.GlobalTransportes.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Set;

@Component
public class ManipuladorAutenticacao implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/home");
        } else if (roles.contains("ROLE_MOTORISTA")) {
            response.sendRedirect("/motorista/home");
        } else if (roles.contains("ROLE_CLIENTE")) {
            response.sendRedirect("/cliente/home");
        } else {
            response.sendRedirect("/login?error");
        }
    }
}
