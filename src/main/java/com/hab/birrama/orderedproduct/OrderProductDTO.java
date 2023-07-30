package com.hab.birrama.orderedproduct;

import com.hab.birrama.order.Order;
import com.hab.birrama.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderProductDTO {
    private Order order;
    private List<ProductCountDTO> productCountDTOS;
}
