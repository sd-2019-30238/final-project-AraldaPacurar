package fancyRestaurant.fancyRestaurant.model.decorator;

import fancyRestaurant.fancyRestaurant.model.Cart;

public class FreeBeerCartDecorator extends CartDecorator {

    public FreeBeerCartDecorator(Cart decoratedCart) {

        super(decoratedCart);
    }

    public void setDeals(String deals){

        decoratedCart.setDeals("Free shipping!");
        addFreeBeerDeal();
    }

    private void addFreeBeerDeal(){

        decoratedCart.addDeal("Bonus: Free beer!");
    }
}
