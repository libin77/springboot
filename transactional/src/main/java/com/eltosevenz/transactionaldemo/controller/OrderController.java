package com.eltosevenz.transactionaldemo.controller;

import com.eltosevenz.transactionaldemo.service.OrderRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRequestService orderService;

    @PostMapping("/place")
    public String placeOrder(@RequestParam String productCode, @RequestParam int quantity) {
        return orderService.placeOrder(productCode, quantity);
    }
}
