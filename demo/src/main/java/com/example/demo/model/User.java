package com.example.demo.model;

import com.example.demo.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import lombok.Setter;
import lombok.Getter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "group_id")
    private Long groupId;

    @ManyToMany
    @JoinTable(
            name = "user_objective",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "objective_id")
    )
    @JsonIgnore
    private List<Objective> objectives;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "designation")
    private String designation;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference // This ensures the userRoles are serialized correctly and prevents recursion
    private Set<UserRole> userRoles = new HashSet<>();

    @Column(name = "email", unique = true)
    private String email;

    private String password;

    public User() {
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserVerification userVerification;

    // Constructor without roles
    public User(String username, String designation, long managerId, String email) {
        this.username = username;
        this.managerId = managerId;
        this.designation = designation;
        this.email = email;
    }

    // Add role dynamically using Role enum
    public void addRole(Role role) {
        this.userRoles.add(new UserRole(this, role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (UserRole userRole : this.getUserRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().name()));
        }
        return authorities;
    }

    // Get all roles as a Set of Strings
    public Set<String> getRoles() {
        Set<String> roles = new HashSet<>();
        for (UserRole userRole : userRoles) {
            roles.add(userRole.getRole().name()); // Using name() to get enum string
        }
        return roles;
    }

    // Updated setRoles method using Role enum
    public void setRoles(Set<Role> roles) {
        this.userRoles.clear();
        for (Role role : roles) {
            this.userRoles.add(new UserRole(this, role));
        }
    }

    public void setRoles(Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        setRoles(roles);
    }
}
