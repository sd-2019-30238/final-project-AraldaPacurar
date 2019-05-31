package fancyRestaurant.fancyRestaurant.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FoodType {

    @Id
    @GeneratedValue
    int id;

    @NotNull
    String name;

    @OneToMany
    @JoinColumn(name = "food_type_id")
    private List<Food> foods = new ArrayList<>();

    public FoodType(String name){
        this.name = name;
    }

    public FoodType(){

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
}
