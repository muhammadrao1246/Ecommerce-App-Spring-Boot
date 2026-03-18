package com.muhammadrao1246.SpringDemoApp.repositories;

import com.muhammadrao1246.SpringDemoApp.models.UserLinkedAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLinkedAuthProviderRepository extends JpaRepository<UserLinkedAuthProvider, String> {
}
