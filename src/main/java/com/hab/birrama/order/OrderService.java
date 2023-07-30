package com.hab.birrama.order;


import com.hab.birrama.exception.NotFoundException;
import com.hab.birrama.members.Member;
import com.hab.birrama.members.MemberService;
import com.hab.birrama.orderedproduct.OrderProductDTO;
import com.hab.birrama.orderedproduct.OrderedProduct;
import com.hab.birrama.orderedproduct.OrderedProductId;
import com.hab.birrama.orderedproduct.ProductCountDTO;
import com.hab.birrama.product.Product;
import com.hab.birrama.product.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ProductService productService;

    public Order getOrder(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()){
            orderRepository.deleteById(id);
        } else{
            throw new NotFoundException(
                    "Order with id " + id + " does not exists"
            );
        }
    }
    @Transactional
    public Order addOrder(@NotNull OrderProductDTO orderProductDTO) {

        Order order = orderProductDTO.getOrder();
        System.out.println("order");
        System.out.println(order);

        System.out.println(orderProductDTO.getProductCountDTOS());


        DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        order.setDatetime(dateTimeFormatter.format(now));

        Long memberId = order.getMember().getId();

        Member member = memberService.getMember(memberId);
        if(member != null){
            order.setMember(member);

           Order savedorder = orderRepository.save(order);

            for (ProductCountDTO obj : orderProductDTO.getProductCountDTOS()) {
                Product product = productService.getProduct(obj.getProduct_id());
                OrderedProduct orderedProduct = new OrderedProduct(
                        new OrderedProductId(savedorder.getId(), product.getId()),
                        savedorder,
                        product,
                        obj.getCount()
                );
                savedorder.addOrderedProduct(orderedProduct);
            }

            return savedorder;

        } else{
            throw new NotFoundException(
                    "Member with id " + memberId + "not found"
            );
        }

    }

    public void updateOrder(Integer id, OrderUpdateReqDTO request) {
        Optional<Order> theOrder = orderRepository.findById(id);
        if(theOrder.isPresent()){
            Order updateOrder = theOrder.get();

            updateOrder.setDeliveryStatus(request.getDeliveryStatus());
            updateOrder.setPaymentStatus(request.getPaymentStatus());
            updateOrder.setRemark(request.getRemark());

            orderRepository.save(updateOrder);
        }else{
            throw new NotFoundException(
                    "Order with id " + id + " does not exists"
            );
        }

    }
}
