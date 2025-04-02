package com.example.demo.service;
import com.example.demo.enums.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void assignRolesToUser(Long userId, Set<Role> roles) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        System.out.println("user found : " + user);
        // Remove only the roles being updated instead of clearing all roles
        user.getUserRoles().removeIf(userRole -> roles.contains(userRole.getRole()));

        for (Role role : roles) {
            if (user.getUserRoles().stream().noneMatch(userRole -> userRole.getRole().equals(role))) {
                user.getUserRoles().add(new UserRole(user, role));
            }
        }
        userRepository.save(user);
    }

    public Set<Role> getUserRoles(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        return user.getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toSet());
    }

    @Transactional
    public void removeRoleFromUser(Long userId, Role role) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        user.getUserRoles().removeIf(userRole -> userRole.getRole().equals(role));
        userRepository.save(user);
    }

    public List<Role> getAllRoles() {
        return userRoleRepository.findAllRoles();
    }
}
