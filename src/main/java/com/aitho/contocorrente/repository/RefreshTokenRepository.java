package com.aitho.contocorrente.repository;

import com.aitho.contocorrente.model.Customer;
import com.aitho.contocorrente.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("refreshTokenRepository")
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  int deleteByCustomer(Customer customer);
}
