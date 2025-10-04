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

    public ShoppingCartServiceImpl(UserService userService,
                                   ProductOrderRepository productOrderRepository,
                                   OrderRepository orderRepository,
                                   MailService mailService) {
        this.userService = userService;
        this.productOrderRepository = productOrderRepository;
        this.orderRepository = orderRepository;
        this.mailService = mailService;
    }

    @Override
    public void addProduct(Product product) {
        cart.merge(product, 1, Integer::sum);
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
        return cart.entrySet().stream()
                .mapToDouble(entry -> entry.getValue() *
                        (entry.getKey().getPromoPrice() == null
                                ? entry.getKey().getPrice()
                                : entry.getKey().getPromoPrice()))
                .sum();
    }

    @Override
    public void checkOut(String userEmail) {
        checkOut(userEmail, null, null, null, null);
    }

    @Override
    public void checkOut(String userEmail, String firstName, String lastName, String address, String phone) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserProfile userProfile = user.getUserProfile();
        if (userProfile == null) {
            userProfile = new UserProfile();
        }

        if (firstName != null) userProfile.setFirsName(firstName);
        if (lastName != null) userProfile.setLastName(lastName);
        if (address != null) userProfile.setAddress(address);
        if (phone != null) userProfile.setPhoneNumber(phone);


        user.setUserProfile(userProfile);
        userService.save(user);
        Order newOrder = new Order();
        newOrder.setOrderNumber(ORDER_PREFIX + UUID.randomUUID().toString().substring(0, 8));
        newOrder.setDateOfOrder(new Date());
        newOrder.setStatus(OrderStatus.CREATED);
        newOrder.setTotalAmount(totalPrice());
        newOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        newOrder.setUserProfile(userProfile);
        newOrder.setCustomerName(
                (firstName != null ? firstName : userProfile.getFirsName()) + " " +
                        (lastName != null ? lastName : userProfile.getLastName())
        );
        newOrder.setCustomerAddress(address != null ? address : userProfile.getAddress());
        newOrder.setCustomerPhone(phone != null ? phone : userProfile.getPhoneNumber());

        orderRepository.save(newOrder);

        // Salveaza produsele din coș
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
                    "Pizza Shop - Order Confirmation",
                    "Thank you, " +
                            Optional.ofNullable(firstName).orElse(userProfile.getFirsName()) + " " +
                            Optional.ofNullable(lastName).orElse(userProfile.getLastName()) +
                            "! Your order (" + newOrder.getOrderNumber() + ") has been placed.\n" +
                            "Delivery Address: " + Optional.ofNullable(address).orElse(userProfile.getAddress()) +
                            "\nPhone: " + Optional.ofNullable(phone).orElse(userProfile.getPhoneNumber()) +
                            "\nTotal Amount: " + totalPrice() + " lei"
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
