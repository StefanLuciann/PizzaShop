package org.sda.com.pizzashop.repository;


import org.sda.com.pizzashop.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
}
