package com.neuroassist.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemoryData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Patient patient;

    @Column(length = 100)
    private String category; // e.g., family, work, places

    @Column(length = 200)
    private String title; // e.g., Spouse name, Favorite food

    @Column(length = 2000)
    private String detail; // e.g., "Alice", "Pizza", "Worked at..."
}
