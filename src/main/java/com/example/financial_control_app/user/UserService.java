package com.example.financial_control_app.user;

import com.example.financial_control_app.dto.user.UserCreationDTO;
import com.example.financial_control_app.exception.user.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(UserCreationDTO userCreationDTO) {
        boolean existsByUsername = userRepository.existsByUsername(userCreationDTO.username());
        boolean existsByEmail = userRepository.existsByEmail(userCreationDTO.email());

        if (existsByUsername || existsByEmail) {
            throw new UserAlreadyExistsException("User already exists");
        }

        String hash = passwordEncoder.encode(userCreationDTO.password());
        UserModel userToCreate = new UserModel(userCreationDTO.username(), userCreationDTO.email(), hash);
        userRepository.save(userToCreate);
    }
}
