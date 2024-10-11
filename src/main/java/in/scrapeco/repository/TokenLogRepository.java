package in.scrapeco.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.scrapeco.entity.TokenLog;
import in.scrapeco.entity.enums.TokenLogPurpose;
import in.scrapeco.entity.enums.UserType;

public interface TokenLogRepository extends JpaRepository<TokenLog, Integer> {

	Optional<TokenLog> findFirstByUserIdAndUserTypeAndPurposeAndIsValid(String userId, UserType userType,
			TokenLogPurpose purpose, int isValid);

	Optional<TokenLog> findFirstByToken(String token);

}
