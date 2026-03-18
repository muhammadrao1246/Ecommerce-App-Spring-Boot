package com.muhammadrao1246.SpringDemoApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.generator.EventType;

import java.time.Instant;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(
        name = "orders",
        indexes = {
                @Index(name = "status_index", columnList = "status")
        }
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // which is going to be unique and null
    @Column(insertable = false, columnDefinition = "SERIAL", unique = true, name = "order_number")
    @org.hibernate.annotations.Generated(event = EventType.INSERT)
    private Long orderNumber; // a human readable order number

    @Column(nullable = false, name = "total_amount")
    private Integer totalAmount = 0;

    @Column(nullable = false, length = 50)
    private String status = "Created";

    @CreationTimestamp
    private Instant createdAt;

    private Instant confirmedAt;
    private Instant shippedAt;
    private Instant deliveredAt;

    // Order can be created by customer or by admin on behalf of customer
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "order_user_fk"))
    @JsonManagedReference
    private User user;

    // mapping relation ships
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonBackReference
    private Set<OrderProduct> products = new HashSet<>();
}
