package com.muhammadrao1246.SpringDemoApp.repositories;

import com.muhammadrao1246.SpringDemoApp.models.UserPermissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionsRepository extends JpaRepository<UserPermissions, Long> {
}