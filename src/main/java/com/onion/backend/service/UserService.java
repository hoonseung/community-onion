package com.onion.backend.service;

import com.onion.backend.dto.user.User;
import com.onion.backend.dto.user.UserSignUpRequest;
import com.onion.backend.entity.UserEntity;
import com.onion.backend.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional
    public User signup(UserSignUpRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        UserEntity userPs = userRepository.save(
            UserEntity.of(request.username(), request.password(),
                request.email()));

        return User.from(userPs);
    }


    @Transactional
    public User secession(Long id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        userRepository.delete(user);

        return User.from(user);
    }


}
