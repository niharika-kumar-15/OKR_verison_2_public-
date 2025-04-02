package com.example.demo.model;

import com.example.demo.enums.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference  // This prevents the recursive serialization of the User object
    private User user;

    @Enumerated(EnumType.STRING) // Store the role as a string representation of the enum
    @Column(name = "role")
    private Role role;

    // Default constructor
    public UserRole() {
    }

    // Constructor with User and Role
    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "roleId=" + roleId +
                ", user=" + user +
                ", role=" + role +
                '}';
    }
}