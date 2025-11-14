package com.ms.e_project.Repository;

import com.ms.e_project.Entities.User;
import com.ms.e_project.Entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String email);
    User findByRole(UserRole role);
}
