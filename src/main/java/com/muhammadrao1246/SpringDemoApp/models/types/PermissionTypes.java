package com.muhammadrao1246.SpringDemoApp.models.types;

import lombok.Getter;

@Getter
public enum PermissionTypes {
    CREATE("create"),
    READ("read"),
    UPDATE("update"),
    DELETE("delete"),

    // BELOW ONES ARE FOR THE HARDCODED LOGIC NOW THEY ARE IGNORED
    PRODUCT_READ("product:read"),
    PRODUCT_WRITE("product:write"),
    PRODUCT_ALL("product:all"),
    BRAND_READ("brand:read"),
    BRAND_WRITE("brand:write"),
    BRAND_ALL("brand:all"),
    CATEGORY_READ("category:read"),
    CATEGORY_WRITE("category:write"),
    CATEGORY_ALL("category:all"),
    ORDERS_READ("orders:read"),
    ORDERS_WRITE("orders:write"),
    ORDERS_ALL("orders:all"),
    ;


    private final String permission;
    PermissionTypes(String permission){
        this.permission = permission;
    }

}
