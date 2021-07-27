package yte.intern.project.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.security.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByAuthority(String authority);
}
