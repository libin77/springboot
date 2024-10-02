package com.eltosevenz.transactionaldemo.controller;

import com.eltosevenz.transactionaldemo.exception.InvalidStockException;
import com.eltosevenz.transactionaldemo.model.ProductList;
import com.eltosevenz.transactionaldemo.service.ProductListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductListService productService;

    @GetMapping("/list")
    public List<ProductList> getProductList() {
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    public ProductList addProduct(@RequestBody ProductList product) throws InvalidStockException {
        return productService.createProduct(product);
    }

    @PostMapping("/addByManualTrans")
    public ProductList addProductByManual(@RequestBody ProductList product)  {
        return productService.programmaticTransaction(product);
    }

    @PostMapping("/update")
    public ProductList updateStock(@RequestParam long productId, @RequestParam int stock)  {
        return productService.updateStock(productId, stock);
    }
}
