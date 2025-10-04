package org.sda.com.pizzashop.controller;

import org.sda.com.pizzashop.model.Product;
import org.sda.com.pizzashop.service.ProductService;
import org.sda.com.pizzashop.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
    }

    @GetMapping("/shopping-cart-add/{id}")
    public String addProductToShoppingCart(@PathVariable("id") int id, @RequestParam String origin) {
        productService.findById(id).ifPresent(shoppingCartService::addProduct);
        if ("shopping-cart".equals(origin)) {
            return "redirect:/shopping-cart";
        }
        return "redirect:/home?productAddedToCart";
    }

    @GetMapping("/shopping-cart-remove/{id}")
    public String removeProductFromShoppingCart(@PathVariable("id") int id) {
        productService.findById(id).ifPresent(shoppingCartService::removeProduct);
        return "redirect:/shopping-cart";
    }

    @GetMapping("/shopping-cart")
    public String showShoppingCart(Model model) {
        model.addAttribute("products", shoppingCartService.getAllProducts());
        model.addAttribute("totalPrice", shoppingCartService.totalPrice());
        model.addAttribute("deliveryStart", LocalDateTime.now().plusHours(1));
        model.addAttribute("deliveryEnd", LocalDateTime.now().plusHours(2));
        return "shopping-cart";
    }

    @PostMapping("/cart-checkout")
    public String checkOutCart(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String address,
                               @RequestParam String phone,
                               Principal principal) {
        shoppingCartService.checkOut(principal.getName(), firstName, lastName, address, phone);
        return "redirect:/home?orderPlaced";
    }

}
