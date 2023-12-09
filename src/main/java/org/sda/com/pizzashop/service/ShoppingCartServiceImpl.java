package org.sda.com.pizzashop.service;

import org.sda.com.pizzashop.model.*;
import org.sda.com.pizzashop.model.enums.OrderStatus;
import org.sda.com.pizzashop.model.enums.PaymentMethod;
import org.sda.com.pizzashop.repository.OrderRepository;
import org.sda.com.pizzashop.repository.ProductOrderRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.MessagingException;
import java.util.*;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private static final String ORDER_PREFIX = "PZORD-";
    private final Map<Product, Integer> cart = new LinkedHashMap<>();
    private final UserService userService;
    private final ProductOrderRepository productOrderRepository;
    private final OrderRepository orderRepository;
    private final MailService mailService;

    public ShoppingCartServiceImpl(UserService userService, ProductOrderRepository productOrderRepository, OrderRepository orderRepository, MailService mailService) {
        this.userService = userService;
        this.productOrderRepository = productOrderRepository;
        this.orderRepository = orderRepository;
        this.mailService = mailService;
    }

    @Override
    public void addProduct(Product product) {
        if (cart.containsKey(product)) {
            cart.put(product, cart.get(product) + 1);
        } else {
            cart.put(product, 1);
        }
    }

    @Override
    public void removeProduct(Product product) {

        if (cart.containsKey(product)) {
            if (cart.get(product) == 1) {
                cart.remove(product);
            } else {
                cart.put(product, cart.get(product) - 1);
            }
        }
    }

    @Override
    public void clearProducts() {
        cart.clear();
    }

    @Override
    public double totalPrice() {
        double totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            if (entry.getKey().getPromoPrice() == null) {
                totalPrice += entry.getValue() * entry.getKey().getPrice();
            } else {
                totalPrice += entry.getValue() * entry.getKey().getPromoPrice();
            }
        }
        return totalPrice;
    }

    @Override
    public void checkOut(String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserProfile userProfile = user.getUserProfile();
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile does not exists");
        }

        Order newOrder = new Order();
        newOrder.setOrderNumber(ORDER_PREFIX + new Random().nextInt(1000000));
        newOrder.setDateOfOrder(new Date());
        newOrder.setStatus(OrderStatus.CREATED);
        newOrder.setTotalAmount(totalPrice());
        newOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        newOrder.setUserProfile(userProfile);
        orderRepository.save(newOrder);

        for (Map.Entry<Product, Integer> entry : getAllProducts().entrySet()) {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setOrder(newOrder);
            productOrder.setProduct(entry.getKey());
            productOrder.setQuantity(entry.getValue());
            productOrderRepository.save(productOrder);
        }
        try {
            mailService.sendEmail(
                    "pizzashop@gmail.com",
                    userEmail,
                    "New pizza order",
                    "New pizza order with total amount :" +totalPrice()
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        cart.clear();
    }

    @Override
    public Map<Product, Integer> getAllProducts() {
        return Collections.unmodifiableMap(cart);
    }

}
