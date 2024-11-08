package com.example.module_6_back_end.resources;

import com.example.module_6_back_end.model.JwtResponse;
import com.example.module_6_back_end.model.User;
import com.example.module_6_back_end.service.UserInfoService;
import com.example.module_6_back_end.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserInfoService userInfoService;

    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                           UserInfoService userInfoService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userInfoService = userInfoService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<JwtResponse> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User userInfo = userInfoService.findByEmail(user.getEmail());
        return ResponseEntity.ok(new JwtResponse(jwt, userInfo.getEmail(), userDetails.getAuthorities()));
    }
}
