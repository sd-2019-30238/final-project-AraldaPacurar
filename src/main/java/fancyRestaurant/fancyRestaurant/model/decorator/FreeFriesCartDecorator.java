package fancyRestaurant.fancyRestaurant.model.decorator;

import fancyRestaurant.fancyRestaurant.model.Cart;

public class FreeFriesCartDecorator extends CartDecorator{


    public FreeFriesCartDecorator(Cart decoratedCart) {

        super(decoratedCart);
    }

    public void setDeals(String deals){

        decoratedCart.setDeals("Free shipping!");
        addFreeFriesDeal();
    }

    private void addFreeFriesDeal(){

        decoratedCart.addDeal("Bonus: Free fries!");
    }
}
