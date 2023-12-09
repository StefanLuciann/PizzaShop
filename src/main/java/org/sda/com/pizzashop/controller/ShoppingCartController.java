package org.sda.com.pizzashop.controller;

import org.sda.com.pizzashop.model.Product;
import org.sda.com.pizzashop.service.ProductService;
import org.sda.com.pizzashop.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            shoppingCartService.addProduct(productOptional.get());
        }
        if (origin != null && origin.equals("shopping-cart")) {
            return "redirect:/shopping-cart";
        }
        return "redirect:/home?productAddedToCart";
    }

    @GetMapping("/shopping-cart-remove/{id}")
    public String removeProductFromShoppingCart(@PathVariable("id") int id) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            shoppingCartService.removeProduct(productOptional.get());
        }
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
    @GetMapping("/cart-checkout")
    public String checkOutCart(Principal principal){
        shoppingCartService.checkOut(principal.getName());
        return "redirect:/home";
    }

}
