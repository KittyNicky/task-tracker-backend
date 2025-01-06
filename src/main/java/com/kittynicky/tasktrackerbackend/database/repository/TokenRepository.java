package com.kittynicky.tasktrackerbackend.database.repository;

import com.kittynicky.tasktrackerbackend.database.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessToken(String accessToken);

    Optional<Token> findByRefreshToken(String refreshToken);

    @Query(value = "SELECT t.id, t.user_id, t.access_token, t.refresh_token, t.is_logged_out " +
                   "FROM public.tokens AS t " +
                   "INNER JOIN public.users AS u ON t.user_id = u.id " +
                   "WHERE t.user_id = :userId " +
                   "      AND t.is_logged_out = false;",
            nativeQuery = true)
    List<Token> findAllByUser(Long userId);
}
