package in.scrapeco.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.scrapeco.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);
}
