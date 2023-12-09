package org.sda.com.pizzashop.service;

import org.sda.com.pizzashop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    void create(Product product);

    void update(int id, Product product);
    void delete(int id);

    Optional<Product> findById(int id);

}
