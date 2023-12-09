package org.sda.com.pizzashop.repository;

import org.sda.com.pizzashop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}
