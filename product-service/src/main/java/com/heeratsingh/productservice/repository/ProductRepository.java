package com.heeratsingh.productservice.repository;

import com.heeratsingh.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {
}
