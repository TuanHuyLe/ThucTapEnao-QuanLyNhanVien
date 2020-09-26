package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.messages.JwtResponse;
import com.enao.team2.quanlynhanvien.dto.LoginDTO;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginRequest) {
        String jwt;
        try {
            //authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //generate jwt
            jwt = jwtUtils.generateJwtToken(authentication);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("The username or password was not correct!"), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

}
