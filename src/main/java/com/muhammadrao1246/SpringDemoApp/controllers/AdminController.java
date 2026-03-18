package com.muhammadrao1246.SpringDemoApp.controllers;

import com.muhammadrao1246.SpringDemoApp.models.User;
import com.muhammadrao1246.SpringDemoApp.models.UserPermissions;
import com.muhammadrao1246.SpringDemoApp.models.UserRoles;
import com.muhammadrao1246.SpringDemoApp.models.types.PermissionTypes;
import com.muhammadrao1246.SpringDemoApp.models.types.ResourceTypes;
import com.muhammadrao1246.SpringDemoApp.models.types.RoleTypes;
import com.muhammadrao1246.SpringDemoApp.repositories.UserPermissionsRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.UserRepository;
import com.muhammadrao1246.SpringDemoApp.repositories.UserRolesRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserRolesRepository userRolesRepository;
    private final UserPermissionsRepository userPermissionsRepository;
    private final UserRepository userRepository;


    // ROLES RELATED ROUTES

    @GetMapping("/roles")
    public ResponseEntity<List<UserRoles>> getAllRoles(){
        return ResponseEntity.ok(userRolesRepository.findAll());
    }

    @PostMapping("/roles")
    public ResponseEntity<UserRoles> addRole(@RequestBody RoleTypes role){
        return ResponseEntity.ok(userRolesRepository.save(UserRoles.builder().role(role).build()));
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<UserRoles> getRoleById(@PathVariable Long id){
        return ResponseEntity.ok(userRolesRepository.findById(id).orElseThrow());
    }

    @PutMapping("/roles/{id}/permissions")
    @Transactional
    public ResponseEntity<Set<UserPermissions>> addPermissionToRoleById(@PathVariable Long id, @RequestParam Long permId){
        UserRoles role = userRolesRepository.findById(id).orElseThrow();
        UserPermissions up = userPermissionsRepository.findById(id).orElseThrow();
        role.getPermissions().add(up);
        return ResponseEntity.ok(role.getPermissions());
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable Long id){
        userRolesRepository.delete(userRolesRepository.findById(id).orElseThrow());
        return ResponseEntity.noContent().build();
    }

    // get role permissions
    @GetMapping("roles/{id}/permissions")
    public ResponseEntity<Set<UserPermissions>> getUserRolePermissions(@PathVariable Long id){
        return ResponseEntity.ok(userRolesRepository.findById(id).orElseThrow().getPermissions());
    }

    // USER RELATED ROLES AND PERMISSIONS

    @GetMapping("/user/{id}/roles")
    public ResponseEntity<Set<UserRoles>> getUserRoles(@PathVariable UUID id){
        User user = userRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(user.getRoles());
    }

    @PostMapping("/user/{id}/roles")
    @Transactional
    public ResponseEntity<User> assignRoleToUser(@PathVariable UUID id, @RequestParam Long roleId){
        User user = userRepository.findById(id).orElseThrow();
        UserRoles role = userRolesRepository.findById(roleId).orElseThrow();
        user.getRoles().add(role);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/{id}/roles")
    @Transactional
    public ResponseEntity<User> unassignRoleToUser(@PathVariable UUID id, @RequestParam Long roleId){
        User user = userRepository.findById(id).orElseThrow();
        UserRoles role = userRolesRepository.findById(roleId).orElseThrow();
        user.getRoles().remove(role);
        return ResponseEntity.ok(user);
    }


    /* PERMISSIONS */

    @GetMapping("/permissions")
    public ResponseEntity<List<UserPermissions>> getAllPermissions(){
        return ResponseEntity.ok(userPermissionsRepository.findAll());
    }

    @GetMapping("/permissions/{id}")
    public ResponseEntity<UserPermissions> getPermissionById(@PathVariable Long id){
        return ResponseEntity.ok(userPermissionsRepository.findById(id).orElseThrow());
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<?> deletePermissionById(@PathVariable Long id){
        userPermissionsRepository.delete(userPermissionsRepository.findById(id).orElseThrow());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/permissions")
    public ResponseEntity<UserPermissions> addPermission(@RequestParam @Valid ResourceTypes resource, @RequestParam @Valid PermissionTypes permission){
        UserPermissions up = UserPermissions.builder().resource(resource).permission(permission).build();
        up = userPermissionsRepository.save(up);
        return ResponseEntity.ok(up);
    }



}
