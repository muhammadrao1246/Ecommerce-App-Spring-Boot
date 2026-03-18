package com.muhammadrao1246.SpringDemoApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

// registering this class or object as Entity in Spring JPA so it can be created as a table in db
@Entity
// these are lombok utils that declare setter/getters and constructors for us
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // making a primary key

    @Column(nullable = false, length = 100, comment = "Product Name")
    private String name;

    @Column(length = 300)
    private String description;

    @Column(nullable = false, check = @CheckConstraint(name = "price_check", constraint = "price > 0"))
    private BigDecimal price;
    @Column(nullable = false, check = @CheckConstraint(name = "quantity_check", constraint = "quantity >= 0"))
    private BigDecimal quantity;

    @Lob
    @ToString.Exclude
    @JsonIgnore
    private byte[] imageData = new byte[0];
    @ToString.Exclude
    private String imageType;
    @ToString.Exclude
    private String imageName;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyy")
    @CreationTimestamp
    private Instant createdAt;


    // Connecting relations with other entities
    @ManyToOne(targetEntity = Brand.class) // many products can come under one brand
    // we have to define the foreign key column going to contain the id of an external associated entity along with constraint name
    @JoinColumn(name = "brand_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_brand"))
    @JsonManagedReference
    private Brand brand; // every product should belong to a brand or a store

    @ManyToOne
    @JoinColumn(
            name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_category")
    )
    @JsonManagedReference
    private Category category;

    // order mapping
    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    @JsonBackReference
    private Set<OrderProduct> orders = new HashSet<>();

}
