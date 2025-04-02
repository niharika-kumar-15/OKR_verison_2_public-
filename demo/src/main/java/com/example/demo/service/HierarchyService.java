package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.implementations.BaseEmployee;
import com.example.demo.implementations.ProgramDirector;
import com.example.demo.interfaces.UserComponent;
import java.util.List;
import java.util.Set;

@Service
public class HierarchyService {
    @Autowired
    private UserRepository userRepository;

    public UserComponent getUserHierarchy(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }

        // Fetching the role of the user from the userRoles set
        Set<String> roles = user.getRoles(); // Assuming getRoles() returns a Set<String> of roles
        if (roles.contains("BaseEmployee")) {
            return new BaseEmployee(user);
        }

        return buildHierarchy(user);
    }

    private UserComponent buildHierarchy(User user) {
        // Assume ProgramDirector is the root user in the hierarchy for this case
        UserComponent hierarchy = new ProgramDirector(user);
        List<User> subordinates = userRepository.findUsersByManagerId(user.getUserId());

        for (User subordinate : subordinates) {
            Set<String> roles = subordinate.getRoles();  // Get the roles of the subordinate

            // Check if the subordinate is a ProgramDirector or any other role you need to handle
            if (roles.contains("ProgramDirector")) {
                UserComponent uc = buildHierarchy(subordinate);  // Recursively build hierarchy
                hierarchy.addUser(uc);
            }
        }

        return hierarchy;
    }
}
