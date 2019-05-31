package fancyRestaurant.fancyRestaurant.dao;

import fancyRestaurant.fancyRestaurant.model.Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface FoodDAO extends CrudRepository<Food, Integer> {

    List<Food> findByName(String name);
    List<Food> findByPrice(int price);
    List<Food> findByFoodTypeId(int foodTypeId);
}
