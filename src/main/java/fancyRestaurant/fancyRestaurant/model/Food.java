package fancyRestaurant.fancyRestaurant.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Food {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 1, message = "Food name must not be empty!")
    private String name;

    @NotNull
    @Size(min = 3, message = "Description must not be empty!")
    private String description;

    @NotNull
    private int price;

    @NotNull
    private int originalPrice;

    @OneToMany
    @JoinColumn(name = "food_id")
    private List<Orders> orders = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "food_id")
    private List<ShoppingCart> shoppingCart = new ArrayList<>();

    @ManyToOne
    private FoodType foodType;

    public Food(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Food() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }
}
