package org.sda.com.pizzashop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pizza_sauces")
public class PizzaSauce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Double price;

    public PizzaSauce() {}

    public PizzaSauce(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
