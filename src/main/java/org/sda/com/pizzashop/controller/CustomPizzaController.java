package org.sda.com.pizzashop.controller;

import org.sda.com.pizzashop.model.Product;
import org.sda.com.pizzashop.model.PizzaTopping;
import org.sda.com.pizzashop.model.PizzaSauce;
import org.sda.com.pizzashop.model.enums.ProductCategory;
import org.sda.com.pizzashop.repository.PizzaSauceRepository;
import org.sda.com.pizzashop.repository.PizzaToppingRepository;
import org.sda.com.pizzashop.service.ProductService;
import org.sda.com.pizzashop.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/create-pizza")
public class CustomPizzaController {

    private final PizzaToppingRepository toppingRepository;
    private final PizzaSauceRepository sauceRepository;
    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;

    public CustomPizzaController(PizzaToppingRepository toppingRepository,
                                 PizzaSauceRepository sauceRepository,
                                 ProductService productService,
                                 ShoppingCartService shoppingCartService) {
        this.toppingRepository = toppingRepository;
        this.sauceRepository = sauceRepository;
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public String showForm(Model model,
                           @RequestParam(required = false) String base,
                           @RequestParam(required = false) String size,
                           @RequestParam(required = false) List<Integer> toppingIds,
                           @RequestParam(required = false) List<Integer> sauceIds) {

        double price = calculatePrice(base, size, toppingIds, sauceIds);

        model.addAttribute("toppings", toppingRepository.findAll());
        model.addAttribute("sauces", sauceRepository.findAll());
        model.addAttribute("price", price);

        return "createPizza";
    }

    @PostMapping("/preview")
    public String previewPrice(@RequestParam String base,
                               @RequestParam String size,
                               @RequestParam(required = false) List<Integer> toppingIds,
                               @RequestParam(required = false) List<Integer> sauceIds,
                               Model model) {
        return showForm(model, base, size, toppingIds, sauceIds);
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam String base,
                            @RequestParam String size,
                            @RequestParam(required = false) List<Integer> toppingIds,
                            @RequestParam(required = false) List<Integer> sauceIds,
                            @RequestParam(required = false) String name) {

        double price = calculatePrice(base, size, toppingIds, sauceIds);

        String toppingsText = (toppingIds != null && !toppingIds.isEmpty())
                ? toppingRepository.findAllById(toppingIds).stream()
                .map(PizzaTopping::getName)
                .collect(Collectors.joining(", "))
                : "no toppings";

        String saucesText = (sauceIds != null && !sauceIds.isEmpty())
                ? sauceRepository.findAllById(sauceIds).stream()
                .map(PizzaSauce::getName)
                .collect(Collectors.joining(", "))
                : "no sauces";

        String description = "Base: " + base +
                ", Size: " + size +
                ", Toppings: " + toppingsText +
                ", Sauces: " + saucesText;

        Product custom = new Product();
        custom.setName((name == null || name.isBlank()) ? "Custom Pizza" : name);
        custom.setCategory(ProductCategory.PIZZA);
        custom.setDescription(description);
        custom.setPrice(price);
        custom.setPromoPrice(null);
        custom.setMainImageUrl("/images/custom-pizza.png");
        custom.setCustom(true);

        Product persisted = productService.save(custom);
        shoppingCartService.addProduct(persisted);

        return "redirect:/shopping-cart";
    }


    private double calculatePrice(String base, String size,
                                  List<Integer> toppingIds, List<Integer> sauceIds) {
        double price = 0.0;

        // Base
        if ("classic".equalsIgnoreCase(base)) {
            price += 10;
        } else if ("thin".equalsIgnoreCase(base)) {
            price += 13;
        } else if ("cheese-burst".equalsIgnoreCase(base)) {
            price += 15;
        }

        // Size
        if ("small".equalsIgnoreCase(size)) {
            price += 10;
        } else if ("medium".equalsIgnoreCase(size)) {
            price += 15;
        } else if ("large".equalsIgnoreCase(size)) {
            price += 20;
        }


        if (toppingIds != null && !toppingIds.isEmpty()) {
            price += toppingRepository.findAllById(toppingIds).stream()
                    .mapToDouble(PizzaTopping::getPrice)
                    .sum();
        }


        if (sauceIds != null && !sauceIds.isEmpty()) {
            price += sauceRepository.findAllById(sauceIds).stream()
                    .mapToDouble(PizzaSauce::getPrice)
                    .sum();
        }

        return price;
    }
}
