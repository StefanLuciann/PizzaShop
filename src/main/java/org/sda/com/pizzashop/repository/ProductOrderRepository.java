package org.sda.com.pizzashop.repository;

import org.sda.com.pizzashop.model.Product;
import org.sda.com.pizzashop.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder,Integer> {

}
