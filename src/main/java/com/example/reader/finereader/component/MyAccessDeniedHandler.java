package com.example.reader.finereader.component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private static Logger logger = LoggerFactory.getLogger(MyAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            logger.info("User '" + auth.getName() + "' attempted to access the protected URL: " +
                    request.getRequestURI());
        }
        if (request.isUserInRole("ROLE_ADMIN")) {
            response.sendRedirect(request.getContextPath() + "/adminDashboard/dashboard");
        }
        if (request.isUserInRole("ROLE_CONTROL")) {
            response.sendRedirect(request.getContextPath() + "/controlDashboard/dashboard");
        }

    }
}
