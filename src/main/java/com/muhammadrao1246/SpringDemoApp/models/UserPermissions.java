package com.muhammadrao1246.SpringDemoApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.muhammadrao1246.SpringDemoApp.models.types.PermissionTypes;
import com.muhammadrao1246.SpringDemoApp.models.types.ResourceTypes;
import com.muhammadrao1246.SpringDemoApp.models.types.RoleTypes;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_permissions")
@ToString
@Builder
public class UserPermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ResourceTypes resource;

    @Enumerated(EnumType.STRING)
    private PermissionTypes permission;

    // permissions assigned how many roles
    @ManyToMany(mappedBy = "permissions")
    @JsonBackReference
    @ToString.Exclude
    private Set<UserRoles> roles = new HashSet<>();
}
