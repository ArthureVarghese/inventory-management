package com.largegroup.inventory_api.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity (name = "`order`")
public class Order {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @Column (name = "user_id")
    private Integer userId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    @Column (name = "product_id")
    private Integer productId;

    private Double price;

    private Integer quantity;

    private Double total;

}
