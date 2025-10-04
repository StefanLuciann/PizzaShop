package org.sda.com.pizzashop.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "custom_pizzas")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String base;
    private String size;
    private Double price;

    @ManyToMany
    @JoinTable(
            name = "pizza_sauce_mapping",
            joinColumns = @JoinColumn(name = "pizza_id"),
            inverseJoinColumns = @JoinColumn(name = "sauce_id")
    )
    private Set<PizzaSauce> sauces = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "pizza_topping_mapping",
            joinColumns = @JoinColumn(name = "pizza_id"),
            inverseJoinColumns = @JoinColumn(name = "topping_id")
    )
    private Set<PizzaTopping> toppings = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Set<PizzaTopping> getToppings() { return toppings; }
    public void setToppings(Set<PizzaTopping> toppings) { this.toppings = toppings; }

    public Set<PizzaSauce> getSauces() { return sauces; }
    public void setSauces(Set<PizzaSauce> sauces) { this.sauces = sauces; }
}
