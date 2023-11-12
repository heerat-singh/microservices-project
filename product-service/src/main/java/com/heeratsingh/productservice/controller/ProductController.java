package com.heeratsingh.productservice.controller;

import com.heeratsingh.productservice.dto.ProductRequest;
import com.heeratsingh.productservice.dto.ProductResponse;
import com.heeratsingh.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> findAll() {

        return productService.getProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {

        productService.createProduct(productRequest);
    }
}