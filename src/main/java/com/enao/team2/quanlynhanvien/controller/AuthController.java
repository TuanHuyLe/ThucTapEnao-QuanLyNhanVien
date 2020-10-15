package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.constants.RedisKey;
import com.enao.team2.quanlynhanvien.exception.UnauthorizedException;
import com.enao.team2.quanlynhanvien.messages.ApiError;
import com.enao.team2.quanlynhanvien.messages.JwtResponse;
import com.enao.team2.quanlynhanvien.dto.LoginDTO;
import com.enao.team2.quanlynhanvien.utils.JwtUtils;
import com.enao.team2.quanlynhanvien.validatation.ValidateLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private RedisTemplate template;

    /**
     * function login
     *
     * @param loginRequest dto login: username - password
     * @return json web token
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginRequest) {
        List<String> error = ValidateLogin.check(loginRequest);
        if (!error.isEmpty()) {
            ApiError errorMessage = new ApiError(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Login failed!",
                    error
            );
            return ResponseEntity.badRequest().body(errorMessage);
        }
        String jwt;
        //authenticate
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Password is wrong!");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //generate jwt
        jwt = jwtUtils.generateJwtToken(authentication);
        //add jwt to redis
        template.opsForValue().set(RedisKey.JWT, jwt);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    /**
     * function logout
     *
     * @return string if success
     */
    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        template.delete(RedisKey.JWT);
        template.delete(RedisKey.USER_DETAIL);
        return "logout success";
    }

}