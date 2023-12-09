package org.sda.com.pizzashop.service;

import org.sda.com.pizzashop.model.User;
import org.sda.com.pizzashop.model.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> findByEmail(String email);

    void createUser(String email, String password, UserRole role, String name);
}
