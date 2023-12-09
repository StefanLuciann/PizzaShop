package org.sda.com.pizzashop.repository;

import org.sda.com.pizzashop.model.Order;
import org.sda.com.pizzashop.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {

}
