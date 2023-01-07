package com.note.springsecuritynote.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.note.springsecuritynote.user.Member;
import com.note.springsecuritynote.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * JWT를 이용한 인증
 */
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

            // header 에서 JWT token을 가져옵니다.
            String authorizationHeader = request.getHeader("access_token");

//            token = Arrays.stream(request.getCookies())
//                    .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
//                    .map(Cookie::getValue)
//                    .orElse(null);

        if (authorizationHeader == null) {//&& token.startsWith("Bearer ")
            //token=token.substring(7);

                log.info("JwtAuthorizationFilter : JWT Token이 존재하지 않습니다.");
                response.setStatus(SC_BAD_REQUEST);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                ErrorResponse errorResponse = new ErrorResponse(400, "JWT Token이 존재하지 않습니다.");
                new ObjectMapper().writeValue(response.getWriter(), errorResponse);

//                Authentication authentication = getUsernamePasswordAuthenticationToken(token);
//                SecurityContextHolder.getContext().setAuthentication(authentication);

            }else{
            try {
                // Access Token만 꺼내옴
                String accessToken = authorizationHeader;
//                        .substring(TOKEN_HEADER_PREFIX.length());

                Authentication authentication = getUsernamePasswordAuthenticationToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // === Access Token 검증 === //
//                JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
//                DecodedJWT decodedJWT = verifier.verify(accessToken);
//
//                // === Access Token 내 Claim에서 Authorities 꺼내 Authentication 객체 생성 & SecurityContext에 저장 === //
//                List<String> strAuthorities = decodedJWT.getClaim("roles").asList(String.class);
//                List<SimpleGrantedAuthority> authorities = strAuthorities.stream().map(SimpleGrantedAuthority::new).toList();
//                String username = decodedJWT.getSubject();
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                chain.doFilter(request, response);
//            } catch (TokenExpiredException e) {
//                log.info("CustomAuthorizationFilter : Access Token이 만료되었습니다.");
//                response.setStatus(SC_UNAUTHORIZED);
//                response.setContentType(APPLICATION_JSON_VALUE);
//                response.setCharacterEncoding("utf-8");
//                ErrorResponse errorResponse = new ErrorResponse(401, "Access Token이 만료되었습니다.");
//                new ObjectMapper().writeValue(response.getWriter(), errorResponse);
            } catch (Exception e) {
                log.info("CustomAuthorizationFilter : JWT 토큰이 잘못되었습니다. message : {}", e.getMessage());
                response.setStatus(SC_BAD_REQUEST);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                ErrorResponse errorResponse = new ErrorResponse(400, "잘못된 JWT Token 입니다.");
                new ObjectMapper().writeValue(response.getWriter(), errorResponse);
            }

        }
        //chain.doFilter(request, response);
    }

    /**
     * JWT 토큰으로 User를 찾아서 UsernamePasswordAuthenticationToken를 만들어서 반환한다.
     * User가 없다면 null
     */
    private Authentication getUsernamePasswordAuthenticationToken(String token) {
        String userName = JwtUtils.getUsername(token);
        if (userName != null) {
            Member member = userRepository.findByUsername(userName); // 유저를 유저명으로 찾습니다.
            return new UsernamePasswordAuthenticationToken(
                    member, // principal
                    null,
                    member.getAuthorities()
            );
        }
        return null; // 유저가 없으면 NULL
    }
}