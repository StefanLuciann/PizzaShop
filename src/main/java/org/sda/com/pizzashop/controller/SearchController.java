package org.sda.com.pizzashop.controller;


import org.sda.com.pizzashop.model.Product;
import org.sda.com.pizzashop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final ProductService productService;
    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q", required = false) String q, Model model) {
        List<Product> results = (q == null || q.isBlank())
                ? List.of()
                : productService.searchProducts(q.trim());
        model.addAttribute("query", q);
        model.addAttribute("products", results);
        return "search";
    }
}
