package com.sparta.mat_dil_admin.jwt;

import com.sparta.mat_dil_admin.security.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    //토큰 검증
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException{
        String accessToken = jwtUtil.getAccessTokenHeader(req);

        if(StringUtils.hasText(accessToken)){
            try{
                if(jwtUtil.validateToken(accessToken)){
                    String accountId = jwtUtil.getAccountIdFromClaims(accessToken);
                    setAuthentication(accountId);
                }
            } catch (ExpiredJwtException e){
                handleExpiredAccessToken(req, res, e);
            } catch(JwtException | IllegalArgumentException e){
                handleInvalidAccessToken(res);
                return;
            }
        }
        filterChain.doFilter(req, res);
    }

    //리프레시 토큰 검증
    private void handleExpiredAccessToken(HttpServletRequest req, HttpServletResponse res, ExpiredJwtException e) throws IOException {
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(req);

        if(StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)){
            String accountId = jwtUtil.getAccountIdFromClaims(refreshToken);
            String newAccessToken = jwtUtil.createAccessToken(accountId);

            res.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
            res.addHeader(JwtUtil.REFRESH_HEADER, refreshToken);

            setAuthentication(accountId);

            log.info("새로운 토큰 생성 완료!");
        }
        else{
            errorResponse(res, "유효하지 않은 리프레시 토큰입니다.");
        }
    }

    //인증 처리
    private void setAuthentication(String accountId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(accountId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String accountId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(accountId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //유효하지 않은 액세스 토큰이 들어올 경우
    private void handleInvalidAccessToken(HttpServletResponse res) throws IOException {
        errorResponse(res, "유효하지 않은 액세스 토큰입니다.");
    }

    //에러 메세지 응답
    private void errorResponse(HttpServletResponse res, String message) throws IOException{
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = res.getWriter();
        writer.write("{\"message\":\"" + message + "\"}");
        writer.flush();
    }

}
