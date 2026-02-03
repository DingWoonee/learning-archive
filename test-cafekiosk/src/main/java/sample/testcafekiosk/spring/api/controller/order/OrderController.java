package sample.testcafekiosk.spring.api.controller.order;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.testcafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.testcafekiosk.spring.api.service.order.OrderService;
import sample.testcafekiosk.spring.api.service.order.response.OrderResponse;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderservice;

    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime registeredDateTime = LocalDateTime.now();
        return orderservice.createOrder(request, registeredDateTime);
    }
}
