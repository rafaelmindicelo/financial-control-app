package com.example.financial_control_app.category;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tbl_category")
@Getter
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String description;
}
