package com.hab.birrama.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hab.birrama.members.Member;
import com.hab.birrama.orderedproduct.OrderedProduct;
import com.hab.birrama.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "orders"
)
public class Order {

    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "order_sequence"
    )
    @Column(
            name ="id"
    )
    private Integer id;

    @Column(
            name = "order_number"
    )
    private Integer orderNumber;


    @Column(
            name = "payment_option"
    )
    private String paymentOption;

    @Column(
            name = "transaction_number"
    )
    private String transactionNumber;


    private String datetime;

    @Column(
            name = "payment_status"
    )
    @Enumerated(EnumType.STRING)
    private Status paymentStatus;


    @Column(
            name = "delivery_status"
    )
    @Enumerated(EnumType.STRING)
    private Status deliveryStatus;

    private String remark;

    private String comment;

    @Column(
            name ="total_price"
    )
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "member_order_fk"
            )
    )

    private Member member;

    @OneToMany(
            mappedBy = "order",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<OrderedProduct> orderedProducts = new ArrayList<>();

    public void addOrderedProduct(OrderedProduct orderedProduct){
        if(!orderedProducts.contains(orderedProduct)){
            orderedProduct.setOrder(this);
            orderedProducts.add(orderedProduct);

        }
    }

    public void removeOrderedProducts(OrderedProduct orderedProduct){
        orderedProducts.remove(orderedProduct);
    }

}
