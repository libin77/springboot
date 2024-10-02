package com.eltosevenz.transactionaldemo.repository;

import com.eltosevenz.transactionaldemo.model.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductListRepository extends JpaRepository<ProductList, Long> {
    ProductList findByCode(String code);
}