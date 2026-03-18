package com.muhammadrao1246.SpringDemoApp.models.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResourceTypes {
    PRODUCT("product"),
    BRAND("brand"),
    CATEGORY("category"),
    ORDERS("orders")
    ;

    private final String resource;
}
