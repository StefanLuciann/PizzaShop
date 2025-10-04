package org.sda.com.pizzashop.service;

import org.sda.com.pizzashop.model.Product;

import java.util.Map;

public interface ShoppingCartService {
        void addProduct(Product product);
        void removeProduct(Product product);
        void clearProducts();
        double totalPrice();

        void checkOut(String userEmail);

        void checkOut(String userEmail, String firstName, String lastName, String address, String phone);

        Map<Product, Integer> getAllProducts();
}

