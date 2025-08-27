package com.example.financial_control_app.security;

import com.example.financial_control_app.dto.user.UserLoginRequestDTO;
import com.example.financial_control_app.dto.user.UserLoginResponseDTO;
import com.example.financial_control_app.user.UserModel;
import com.example.financial_control_app.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO request) {
        try {
            // 1 - authenticates user/password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            // 2 - recover user from DB
            UserModel user = userRepository.findByUsername(request.username())
                    .orElseThrow(RuntimeException::new);

            // 3 - generates token with claims
            String token = jwtService.generate(
                    user.getId(),
                    user.getAccount() != null ? user.getAccount().getId() : null,
                    request.username()
            );

            // 4 - returns token
            return ResponseEntity.ok(new UserLoginResponseDTO(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
