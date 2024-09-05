package com.onion.backend.entity.jwt.repository;

import com.onion.backend.entity.jwt.JwtBlackListEntity;
import com.onion.backend.entity.jwt.LogoutType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JwtBlackListRepository extends JpaRepository<JwtBlackListEntity, Long> {

    Optional<JwtBlackListEntity> findByToken(String token);

    @Query("select j from JwtBlackListEntity j where j.username = :username and j.logoutType = :type order by j.expiration desc limit 1")
    Optional<JwtBlackListEntity> findRecentLastOneByUsernameAndLogoutType(String username, LogoutType type);
}

