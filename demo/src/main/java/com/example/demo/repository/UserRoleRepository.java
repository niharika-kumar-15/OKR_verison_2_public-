package com.example.demo.repository;

import com.example.demo.enums.Role;
import com.example.demo.model.UserRole;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUser(User user);

    Optional<UserRole> findByUserAndRole(User user, Role role);

    void deleteByUserAndRole(User user, Role role);

    @Query("SELECT ur.user FROM UserRole ur WHERE ur.role = :role")
    List<User> findUsersByRole(@Param("role") Role role);

    @Query("SELECT DISTINCT ur.role FROM UserRole ur") // DISTINCT added to avoid duplicate roles
    List<Role> findAllRoles();
}
