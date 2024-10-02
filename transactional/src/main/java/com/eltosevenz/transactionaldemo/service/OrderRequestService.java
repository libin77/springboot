package com.eltosevenz.transactionaldemo.service;

import com.eltosevenz.transactionaldemo.model.OrderRequest;
import com.eltosevenz.transactionaldemo.model.ProductList;
import com.eltosevenz.transactionaldemo.repository.OrderRequestRepository;
import com.eltosevenz.transactionaldemo.repository.ProductListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderRequestService {

    @Autowired
    private OrderRequestRepository orderRepository;

    @Autowired
    private ProductListRepository productRepository;

    @Transactional
    public String placeOrder(String productCode, int quantity) {
        ProductList product = productRepository.findByCode(productCode);

        // Check if enough stock is available
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + productCode);
        }

        // Create order
        OrderRequest order = new OrderRequest();
        order.setProductCode(productCode);
        order.setQuantity(quantity);
        order.setOrderDate(new Date());
        orderRepository.save(order);

        // Update stock
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        return "Order placed successfully!";
    }
}