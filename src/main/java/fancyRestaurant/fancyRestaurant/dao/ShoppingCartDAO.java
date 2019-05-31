package fancyRestaurant.fancyRestaurant.dao;

import fancyRestaurant.fancyRestaurant.model.ShoppingCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ShoppingCartDAO extends CrudRepository<ShoppingCart, Integer> {

    List<ShoppingCart> findByUserId(int userId);
}
