package com.muhammadrao1246.SpringDemoApp.mappers;

import com.muhammadrao1246.SpringDemoApp.models.UserRoles;
import com.muhammadrao1246.SpringDemoApp.models.types.PermissionTypes;
import static com.muhammadrao1246.SpringDemoApp.models.types.PermissionTypes.*;
import com.muhammadrao1246.SpringDemoApp.models.types.RoleTypes;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RolePermissionMapper {
    public static final Map<RoleTypes, Set<PermissionTypes>> ROLE_PERMISSION_MAP = Map.of(
            RoleTypes.CUSTOMER, Set.of(PRODUCT_READ, BRAND_READ, CATEGORY_READ, ORDERS_READ, ORDERS_WRITE),
            RoleTypes.SELLER, Set.of(PRODUCT_READ, PRODUCT_WRITE, BRAND_READ, BRAND_WRITE, CATEGORY_READ, CATEGORY_WRITE, ORDERS_READ, ORDERS_WRITE),
            RoleTypes.ADMIN, Set.of(PRODUCT_READ, PRODUCT_WRITE, BRAND_READ, BRAND_WRITE, CATEGORY_READ, CATEGORY_WRITE, ORDERS_READ, ORDERS_WRITE)
    );

    public static Set<String> getRoleTypeToPermissionStringSet(RoleTypes role){
        return ROLE_PERMISSION_MAP.get(role).stream().map(
            PermissionTypes::name
        ).collect(Collectors.toSet());
    }

    // NEW
    public static @NonNull Set<SimpleGrantedAuthority> getRoleTypeToGranterAuthoritySet(UserRoles role){
        var aset = role.getPermissions().stream().map( permission ->
                new SimpleGrantedAuthority(permission.getResource().name()+":"+permission.getPermission().name())
        ).collect(Collectors.toSet());
        aset.add(new SimpleGrantedAuthority("ROLE_"+role.getRole().name()));
        return aset;
    }

    // OLD FOR HARDCODED ONES
//    public static @NonNull Set<SimpleGrantedAuthority> getRoleTypeToGranterAuthoritySet(RoleTypes role){
//        var aset = ROLE_PERMISSION_MAP.get(role).stream().map( permissionTypes ->
//                new SimpleGrantedAuthority(permissionTypes.name())
//        ).collect(Collectors.toSet());
//        aset.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
//        return aset;
//    }
}
