package com.eltosevenz.transactionaldemo.service;

import com.eltosevenz.transactionaldemo.exception.InvalidStockException;
import com.eltosevenz.transactionaldemo.model.ProductList;
import com.eltosevenz.transactionaldemo.repository.ProductListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Service
public class ProductListService {

    @Autowired
    private ProductListRepository productRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    // Rollback on Custom Exceptions. Even norollbackFor can use as opposite
    @Transactional(rollbackFor = InvalidStockException.class)
    public ProductList createProduct(ProductList product) throws InvalidStockException {
        productRepository.save(product);

        // Simulate a custom exception
        if (product.getStock() < 0) {
            throw new InvalidStockException("Stock cannot be negative.");
        }

        return product;
    }

    //Isolation Levels -
    // Isolation.READ_UNCOMMITTED: Allows dirty reads.
    //Isolation.READ_COMMITTED: Prevents dirty reads.
    //Isolation.REPEATABLE_READ: Prevents non-repeatable reads.
    //Isolation.SERIALIZABLE: Ensures full isolation, preventing dirty reads, non-repeatable reads, and phantom reads.
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ProductList updateStock(Long productId, int stock) {
        ProductList product = productRepository.findById(productId).orElseThrow(() ->
                new IllegalArgumentException("Product not found"));

        product.setStock(stock);
        productRepository.save(product);

        return product;
    }

    //Timeouts and Read-Only Transactions
    //You can configure transaction timeouts and mark a transaction as read-only
    //to optimize performance in certain scenarios.
    @Transactional(timeout = 5, readOnly = true)
    public List<ProductList> getAllProducts() {
        return productRepository.findAll();
    }

    //Programmatic Transaction Management
    //Besides using @Transactional at the method level,
    //Spring also allows programmatic control of transactions.
    //This can be useful when you need more fine-grained control over the transaction boundaries.
    public ProductList programmaticTransaction(ProductList product) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            productRepository.save(product);
            // Commit the transaction
            transactionManager.commit(status);
        } catch (Exception e) {
            // Rollback the transaction in case of failure
            transactionManager.rollback(status);
            throw e;
        }

        return product;
    }
}
