package com.muhammadrao1246.SpringDemoApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.muhammadrao1246.SpringDemoApp.mappers.RolePermissionMapper;
import com.muhammadrao1246.SpringDemoApp.models.types.RoleTypes;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@ToString
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = true, unique = true)
    private String username;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    // Roles of the users
//    @Enumerated(EnumType.STRING)
//    // this annotation going to create 1 to Many relation table automatically in the db
//    // where user_roles ( user_id, role ) like structure going to be
//    // it is used for simple embeddable classes or types so we do not have to create a separate entity class for them
//    // also it is going to be fully managed by its parent. purely dependent on the lifecycle of its parent
//    @ElementCollection(fetch = FetchType.EAGER) // fetch all roles when user is fetched
//    private Set<RoleTypes> roles = new HashSet<>();

    // New Logic Users can have many roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_user_mapping",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @JsonManagedReference
    private Set<UserRoles> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Normally one user only have one role but we are going to set permission according to each role of user
        Set<SimpleGrantedAuthority> allPermissions = new HashSet<>();
        roles.forEach(role -> {
            allPermissions.addAll(RolePermissionMapper.getRoleTypeToGranterAuthoritySet(role));
        });
        return allPermissions;
//        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet());
    }

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    @ToString.Exclude
    private List<UserLinkedAuthProvider> linkedAccounts = new ArrayList<>();

    // one to one relationship between wallet and user
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserWallet wallet;

    // one user can do many orders
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    @ToString.Exclude
    private Set<Order> orders = new HashSet<>();
}
