package com.onion.backend.service.user;

import com.onion.backend.dto.user.User;
import com.onion.backend.dto.user.UserLoginRequest;
import com.onion.backend.dto.user.UserSignUpRequest;
import com.onion.backend.entity.user.UserEntity;
import com.onion.backend.entity.user.repository.UserEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional
    public User signup(UserSignUpRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        UserEntity userPs = userRepository.save(
            UserEntity.of(request.username(), passwordEncoder.encode(request.password()),
                request.email()));

        return User.from(userPs);
    }


    @Transactional
    public Long secession(Long id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        userRepository.delete(user);

        return id;
    }


    @Transactional
    public User login(UserLoginRequest request) {
        UserEntity userPs = getUser(request.username());
        if (!passwordEncoder.matches(request.password(), userPs.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        User user = User.from(userPs);
        userPs.refreshLastLogin();

        return user;
    }


    public List<User> getUsers() {
        return userRepository.findAll()
            .stream()
            .map(User::from)
            .toList();
    }


    public User loadUserByUsername(String username) {
        return User.from(getUser(username));
    }


    public UserEntity getUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
    }


}
