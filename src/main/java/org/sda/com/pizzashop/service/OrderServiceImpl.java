package org.sda.com.pizzashop.service;

import org.sda.com.pizzashop.model.Order;
import org.sda.com.pizzashop.model.User;
import org.sda.com.pizzashop.model.UserProfile;
import org.sda.com.pizzashop.repository.OrderRepository;
import org.sda.com.pizzashop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Order> findAllByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserProfile userProfile = user.getUserProfile();
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile does not exist");
        }
        return orderRepository.findAllByUserProfile(userProfile);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findByIdAndUserProfile(int id, UserProfile userProfile) {
        return orderRepository.findByIdAndUserProfile(id, userProfile);
    }

    @Override
    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public void update(Order order) {
        orderRepository.save(order);
    }
}
