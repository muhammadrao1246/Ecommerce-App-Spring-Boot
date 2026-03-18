package com.muhammadrao1246.SpringDemoApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.muhammadrao1246.SpringDemoApp.models.types.AppAuthProviders;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_linked_auth_providers")
@ToString
public class UserLinkedAuthProvider {
    @Id
    private String providerNameId;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_auth_provider_id"))
    @JsonManagedReference
    private User user; // user has providers
}
