package sample.testcafekiosk.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.testcafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.testcafekiosk.spring.api.service.order.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderservice;

    @PostMapping("/api/v1/orders/new")
    public void createOrder(@RequestBody OrderCreateRequest request) {
        orderservice.createOrder(request);
    }
}
