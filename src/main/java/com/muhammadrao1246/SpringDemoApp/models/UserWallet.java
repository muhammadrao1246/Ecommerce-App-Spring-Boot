package com.muhammadrao1246.SpringDemoApp.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_wallet")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserWallet {
    @Id
    // this id is just for reference in actual after @MapsId annotated field will be used as primary key
    // to form one to one relationship with user - wallet
    private UUID id;

    private BigDecimal balance;

    // relation one to one with user entity
    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_wallet"))
    private User user;
}