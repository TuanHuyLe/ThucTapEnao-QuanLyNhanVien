package com.enao.team2.quanlynhanvien.security;

import com.enao.team2.quanlynhanvien.constants.RedisKey;
import com.enao.team2.quanlynhanvien.exception.UnauthorizedException;
import com.enao.team2.quanlynhanvien.service.impl.UserDetailsServiceImpl;
import com.enao.team2.quanlynhanvien.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    ResourceBundle resourceBundle = ResourceBundle.getBundle("jwt");

    private final String AUTHORIZATION = resourceBundle.getString("HEADER_STRING");

    private final String BEARER = resourceBundle.getString("TOKEN_PREFIX");

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisTemplate template;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //get jwt from request
            String jwt = getJwtFromRequest(request);
            UserDetails userDetails;
            if (jwt != null && jwtUtils.validateJwtToken(jwt) && template.opsForValue().get(RedisKey.JWT).equals(jwt)) {
                if (template.hasKey(RedisKey.USER_DETAIL) && template.opsForValue().get(RedisKey.USER_DETAIL) != null) {
                    userDetails = (UserDetails) template.opsForValue().get(RedisKey.USER_DETAIL);
                } else {
                    //get username from jwt
                    String username = jwtUtils.getUsernameFromJwtToken(jwt);
                    //load user detail
                    userDetails = userDetailsService.loadUserByUsername(username);
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //set authenticate
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER + " ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}