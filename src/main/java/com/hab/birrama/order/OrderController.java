package com.hab.birrama.order;

import com.hab.birrama.orderedproduct.OrderProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping(path="{orderId}")
    public Order getOrder(@PathVariable("orderId") Integer id){
        return  orderService.getOrder(id);
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@RequestBody OrderProductDTO orderProductDTO){

         return new ResponseEntity(orderService.addOrder(orderProductDTO), HttpStatus.ACCEPTED);
    }

    @PutMapping("{orderId}")
    public void updateOrder(
                @PathVariable Integer orderId,
                @RequestBody OrderUpdateReqDTO requestDTO
    ){
        orderService.updateOrder(orderId, requestDTO);
    }

    @DeleteMapping(path = "{orderId}")
    public void deleteOrder(@PathVariable("orderId") Integer id){
        orderService.deleteOrder(id);
    }

}
