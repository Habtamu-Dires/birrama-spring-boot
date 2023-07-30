package com.hab.birrama.orderedproduct;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductCountDTO {

        private Integer product_id;
        private Integer count;
}
