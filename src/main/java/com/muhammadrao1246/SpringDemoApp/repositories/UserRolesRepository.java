package com.muhammadrao1246.SpringDemoApp.repositories;

import com.muhammadrao1246.SpringDemoApp.models.UserRoles;
import com.muhammadrao1246.SpringDemoApp.models.types.RoleTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {

    Optional<UserRoles> findByRole(RoleTypes role);

    @Query("select u from UserRoles u where u.role in ?1")
    Set<UserRoles> findAllByRoles(Collection<RoleTypes> roles);
}