package org.sda.com.pizzashop.repository;

import org.sda.com.pizzashop.model.Order;
import org.sda.com.pizzashop.model.Product;
import org.sda.com.pizzashop.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    List<Order> findAllByUserProfile(UserProfile userProfile);
    Optional<Order> findByIdAndUserProfile(int id, UserProfile userProfile);
}
