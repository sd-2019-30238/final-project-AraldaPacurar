package fancyRestaurant.fancyRestaurant.business.factory;

public interface Discount {

    int addDiscount(int crtPrice);
    int removeDiscount(int crtPrice, int initialPrice);
}
