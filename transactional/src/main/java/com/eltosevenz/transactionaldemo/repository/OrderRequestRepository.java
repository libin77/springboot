package com.eltosevenz.transactionaldemo.repository;

import com.eltosevenz.transactionaldemo.model.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {}