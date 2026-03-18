package com.muhammadrao1246.SpringDemoApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.muhammadrao1246.SpringDemoApp.models.types.RoleTypes;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_roles")
@ToString
@Builder
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleTypes role;

    // one role have many permissions
    @ManyToMany(targetEntity = UserPermissions.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_permissions",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")}
    )
    @JsonManagedReference
    @ToString.Exclude
    private Set<UserPermissions> permissions = new HashSet<>();

    // one role can be assigned to many users
    @ManyToMany(targetEntity = User.class, mappedBy = "roles")
    @JsonBackReference
    @ToString.Exclude
    private Set<User> users = new HashSet<>();
}
