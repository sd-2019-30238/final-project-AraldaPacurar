package fancyRestaurant.fancyRestaurant.business.factory;

public class DiscountCreator {

    public Discount createDiscount(String discountType){

        if(discountType.equals("10%")){
            return new DiscountTen();
        }

        return new DiscountTwenty();
    }
}
