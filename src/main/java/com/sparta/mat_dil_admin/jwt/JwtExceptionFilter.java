package com.sparta.mat_dil_admin.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mat_dil_admin.dto.ExceptionDto;
import com.sparta.mat_dil_admin.enums.ErrorType;
import com.sparta.mat_dil_admin.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response); // JwtAuthenticationFilter 실행
        } catch (Exception e) {
            if (e instanceof CustomException CustomException) {
                handleAuthenticationException(response, CustomException.getErrorType());
            }
        }
    }

    private void handleAuthenticationException(HttpServletResponse response, ErrorType errorType) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ExceptionDto(errorType)));
        response.getWriter().flush();
    }
}