package fancyRestaurant.fancyRestaurant.business.factory;

public class DiscountTwenty implements Discount {

    public int addDiscount(int crtPrice) {
        return crtPrice - crtPrice / 5;
    }

    public int removeDiscount(int crtPrice, int initialPrice){
        return crtPrice + initialPrice/5;
    }

    public String toString(){
        return "20% discount";
    }
}
