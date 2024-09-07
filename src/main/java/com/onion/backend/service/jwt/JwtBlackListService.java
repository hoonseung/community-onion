package com.onion.backend.service.jwt;

import com.onion.backend.entity.jwt.JwtBlackListEntity;
import com.onion.backend.entity.jwt.LogoutType;
import com.onion.backend.entity.jwt.repository.JwtBlackListRepository;
import com.onion.backend.security.jwt.JwtProvider;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class JwtBlackListService {

    private final JwtBlackListRepository jwtBlackListRepository;
    private final JwtProvider jwtProvider;


    @Transactional
    public void blackListRegistration(String token, LogoutType type) {
        if (jwtBlackListRepository.findByToken(token).isPresent() ||
            recentLogoutExpirationCompare(jwtProvider.getUsername(token),
                jwtProvider.getExpiration(token))
                .orElse(false)
        ) {
            throw new IllegalStateException("이미 로그아웃 상태입니다.");
        }

        jwtBlackListRepository.save(JwtBlackListEntity.of(token,
            LocalDateTime.now(),
            jwtProvider.getUsername(token),
            type
        ));
    }


    public boolean isBlackListToken(String token) {
        if (jwtBlackListRepository.findByToken(token).
            filter(blackList -> blackList.getLogoutType().equals(LogoutType.SINGLE))
            .isPresent()) {
            return true;
        }
        String username = jwtProvider.getUsername(token);
        LocalDateTime currentExpiration = jwtProvider.getExpiration(token);

        return recentLogoutExpirationCompare(username, currentExpiration)
            .orElse(false);
    }


    private Optional<Boolean> recentLogoutExpirationCompare(String username,
        LocalDateTime currentExpiration) {
        return jwtBlackListRepository
            .findRecentLastOneByUsernameAndLogoutType(username, LogoutType.EVERY)
            .map(
                blackList -> blackList.getExpiration().isAfter(currentExpiration.minusMinutes(30))
            );
    }


}
