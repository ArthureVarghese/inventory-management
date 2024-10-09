package com.largegroup.inventory_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity (name = "product")
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;

    @Column (name = "category_id")
    private Integer categoryId;

    private Double price;

    private Integer quantity;

    private Boolean active;
}
