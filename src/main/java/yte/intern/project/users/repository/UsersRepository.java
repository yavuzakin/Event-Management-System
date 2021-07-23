package yte.intern.project.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.users.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}
