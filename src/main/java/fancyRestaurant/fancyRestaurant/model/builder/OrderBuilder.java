package fancyRestaurant.fancyRestaurant.model.builder;

import fancyRestaurant.fancyRestaurant.model.Food;
import fancyRestaurant.fancyRestaurant.model.Orders;
import fancyRestaurant.fancyRestaurant.model.User;

public class OrderBuilder {

    private Orders order;

    public OrderBuilder(){

        order = new Orders();
    }

    public OrderBuilder setUsername(String username){

        order.setUsername(username);
        return this;
    }

    public OrderBuilder setFoodName(String foodName){

        order.setFoodName(foodName);
        return this;
    }

    public OrderBuilder setUser(User user){

        order.setUser(user);
        return this;
    }

    public OrderBuilder setFood(Food food){

        order.setFood(food);
        return this;
    }

    public OrderBuilder setStatus(String status){
        order.setStatus(status);
        return this;
    }

    public OrderBuilder setFeedback(String feedback){
        order.setFeedback(feedback);
        return this;
    }

    public Orders build(){

        return order;
    }
}
