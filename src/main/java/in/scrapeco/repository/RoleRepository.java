package in.scrapeco.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.scrapeco.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

