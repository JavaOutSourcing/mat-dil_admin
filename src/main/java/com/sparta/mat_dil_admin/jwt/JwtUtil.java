package com.sparta.mat_dil_admin.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    //Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    //리프레시 헤더 값
    public static final String REFRESH_HEADER = "RefreshToken";
    //Token 식별자
    public static final String BEAR ="Bearer ";
    //토큰 만료시간(30분)
    private static final long TOKEN_TIME = 60 * 60 * 1000L;
    //리프레시 토큰 만료시간 (7일)
    private static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L;

    //JWT secret key
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init(){
        byte[] accessKeyBytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(accessKeyBytes);
    }

    // access, refresh 토큰 생성 로직
    private String createToken(String subject, long tokenTime){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenTime);

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, signatureAlgorithm);

        return BEAR + builder.compact();
    }

    // 액세스 토큰 생성
    public String createAccessToken(String username) {
        return createToken(username, TOKEN_TIME);
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String username) {
        return createToken(username, REFRESH_TOKEN_TIME);
    }

    // header에서 accessToken 가져오기
    public String getAccessTokenHeader(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(bearerToken != null && bearerToken.startsWith(BEAR)){
            return bearerToken.substring(BEAR.length());
        }
        return null;
    }

    // header에서 refreshToken 가져오기
    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEAR)) {
            return bearerToken.substring(BEAR.length());
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token){
        return validateTokenInternal(token);
    }

    // 토큰 검증 로직
    private boolean validateTokenInternal(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJwt(token);
            return true;
        }catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명 입니다.", e);
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.", e);
            throw e;
        } catch (Exception e){
            log.error("잘못되었습니다.", e);
            throw e;
        }
    }

    //토큰에서 accountId 가져오기
    public String getAccountIdFromClaims(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody();
        return claims.getSubject();
    }

}
