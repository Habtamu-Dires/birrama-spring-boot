package com.hab.birrama.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateReqDTO {
    Status paymentStatus;
    Status deliveryStatus;
    String remark;
}
