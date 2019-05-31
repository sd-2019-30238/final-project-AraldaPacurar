package fancyRestaurant.fancyRestaurant.model.builder;

import fancyRestaurant.fancyRestaurant.model.Food;
import fancyRestaurant.fancyRestaurant.model.ShoppingCart;

public class ShoppingCartBuilder {

    private ShoppingCart shoppingCart;

    public ShoppingCartBuilder(){

        shoppingCart = new ShoppingCart();
    }

    public ShoppingCartBuilder setFood(Food food){

        shoppingCart.setFood(food);
        return this;
    }

    public ShoppingCartBuilder setUserId(int userId){

        shoppingCart.setUserId(userId);
        return this;
    }

    public ShoppingCartBuilder setPrice(int price){

        shoppingCart.setPrice(price);
        return this;
    }

    public ShoppingCartBuilder setDeals(String deal){

        shoppingCart.setDeals(deal);
        return this;
    }

    public ShoppingCart build(){

        return shoppingCart;
    }

}
