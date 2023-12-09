package org.sda.com.pizzashop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product_order")
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    private Integer id;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "pizza_toppings")
    private String pizzaTopping;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


    public ProductOrder() {
    }

    public ProductOrder(Integer quantity, String pizzaTopping) {
        this.quantity = quantity;
        this.pizzaTopping = pizzaTopping;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPizzaTopping() {
        return pizzaTopping;
    }

    public void setPizzaTopping(String pizzaTopping) {
        this.pizzaTopping = pizzaTopping;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", pizzaTopping='" + pizzaTopping + '\'' +
                ", product=" + product +
                ", order=" + order +
                '}';
    }
}

