package in.scrapeco.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.scrapeco.entity.User;
import jakarta.transaction.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);

	Boolean existsByEmailAndEnabled(String email, Boolean enabled);

}
