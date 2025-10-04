package org.sda.com.pizzashop.repository;

import org.sda.com.pizzashop.model.PizzaTopping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaToppingRepository extends JpaRepository<PizzaTopping, Integer> {
}
