package com.example.module_6_back_end.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Staff staff;
    private double totalPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    @ManyToOne
    private Ground ground;
    @ManyToOne
    private Customer customer;
    private double deposit;
}
