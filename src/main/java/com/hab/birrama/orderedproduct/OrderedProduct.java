
package com.hab.birrama.orderedproduct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hab.birrama.order.Order;
import com.hab.birrama.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "OrderedProduct")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="ordered_products")
@Setter
@Getter
public class OrderedProduct {

    @EmbeddedId
    private OrderedProductId id;

    @ManyToOne
    @MapsId("orderId")
    @JsonIgnore
    @JoinColumn(
            name = "order_id",
            foreignKey = @ForeignKey(
                    name="orderedProduct_order_id_fk"
            )
    )
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(
                    name="orderedProduct_product_id_fk"
            )
    )
    private Product product;

    private Integer count;

}

