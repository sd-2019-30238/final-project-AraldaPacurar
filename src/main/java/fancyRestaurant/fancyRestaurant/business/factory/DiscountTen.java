package fancyRestaurant.fancyRestaurant.business.factory;

public class DiscountTen implements Discount{

    public int addDiscount(int crtPrice) {
        return crtPrice - crtPrice / 10;
    }

    public int removeDiscount(int crtPrice, int initialPrice){
        return crtPrice + initialPrice/10;
    }

    public String toString(){
        return "10% discount";
    }
}
