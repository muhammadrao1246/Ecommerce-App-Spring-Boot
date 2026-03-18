package com.muhammadrao1246.SpringDemoApp.providers;

public interface OAuthProvider {
    default String getUsername() { return null; }

    default String getEmail() { return null; }

    default String getName() { return null; }

    String getId();
}
