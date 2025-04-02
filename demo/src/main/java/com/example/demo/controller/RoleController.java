package com.example.demo.controller;

import com.example.demo.enums.Role;
import com.example.demo.model.User;
import com.example.demo.service.UserRoleService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/roles")
public class RoleController {

    private final UserRoleService userRoleService;

    public RoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping("/assign/{userId}")
    public ResponseEntity<String> assignRole(@PathVariable Long userId, @RequestBody RoleRequest roleRequest) {
        Set<Role> roleEnums = roleRequest.getRoles().stream()
                .map(Role::fromString)
                .collect(Collectors.toSet());
        userRoleService.assignRolesToUser(userId, roleEnums);
        return ResponseEntity.ok("Roles assigned successfully");
    }

    // Create a DTO class to match the request body
    @Setter
    @Getter
    static class RoleRequest {
        private Set<String> roles;

    }


    @GetMapping("/get/{userId}")
    public ResponseEntity<Set<Role>> getUserRoles(@PathVariable Long userId) {
        Set<Role> roles = userRoleService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = userRoleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}
