package com.muhammadrao1246.SpringDemoApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    // Bidirectional reference mapping Product Entity
    // means this Reference will be used to get all brand products
    // Cascade type only purpose is that changes done to parent should propagate to child or not
    // if parent is deleted should child delete also
    // if parent content is updated should child also update at the same transaction
    // if parent is saved should child also save at the same transaction
    // if a parent detach or untrack itself should childs get also detached from EntityManager
    // if a child got dirty should the EntityManager refresh function should refresh childs also that are loaded in parent entity previously
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = false)
    @ToString.Exclude
    @JsonBackReference
    private Set<Product> products =  new HashSet<>(); // should init so orm can fill it
}
