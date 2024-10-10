package in.scrapeco.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.scrapeco.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}

