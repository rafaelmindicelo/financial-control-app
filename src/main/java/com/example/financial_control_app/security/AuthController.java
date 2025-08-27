package com.example.financial_control_app.security;

import com.example.financial_control_app.dto.user.UserLoginRequestDTO;
import com.example.financial_control_app.dto.user.UserLoginResponseDTO;
import com.example.financial_control_app.user.UserModel;
import com.example.financial_control_app.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
        System.out.println("Login attempt for user: " + request.username());
        System.out.println("Password: " + request.password());
        try {
            // 1 - autentica usuário/senha
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            // 2 - recupera usuário do banco
            UserModel user = userRepository.findByUsername(request.username())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 3 - gera token com claims
            String token = jwtService.generate(
                    user.getId(),
                    user.getAccount() != null ? user.getAccount().getId() : null,
                    request.username()
            );

            // 4 - retorna token
            return ResponseEntity.ok(new UserLoginResponseDTO(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
