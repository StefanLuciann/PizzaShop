package org.sda.com.pizzashop.repository;

import org.sda.com.pizzashop.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}
