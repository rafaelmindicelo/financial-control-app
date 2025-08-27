package com.example.financial_control_app.user;

import com.example.financial_control_app.dto.user.UserCreationDTO;
import com.example.financial_control_app.dto.user.UserCreationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserCreationDTO userCreationDTO) {
        try {
            userService.create(userCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserCreationResponseDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
