package com.hab.birrama.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hab.birrama.order.Order;

import com.hab.birrama.orderedproduct.OrderedProduct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name="products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="product_name_unique",
                        columnNames = "product_name")
        }
)
public class Product {
    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "product_sequence"
    )
    @Column(
            name = "product_id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "product_name"
    )
    private String productName;



    private String emoji;


    private String category;

    private String unit;

    @Column(
            name = "unit_selling_price"
    )
    private double unitSellingPrice;

    @Column(
            name = "unit_buying_price"
    )
    private double unitBuyingPrice;


    private String status;


    private Timestamp timestamp;

    @JsonIgnore
    @OneToMany(
            mappedBy = "product",
            cascade = {CascadeType.REMOVE}
    )
    private  List<OrderedProduct> orderedProducts  = new ArrayList<>();


}
