package fancyRestaurant.fancyRestaurant.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ShoppingCart implements Cart{

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Food food;

    @NotNull
    private int price;

    @NotNull
    private int userId;

    @NotNull
    private String deals = "";

    public ShoppingCart(){}

    public int getId() {
        return id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String getDeals() {
        return deals;
    }

    public void setDeals(String deals) {
        this.deals = deals;
    }

    public void addDeal(String deal){
        this.deals = this.deals + " " + deal;
    }
}
