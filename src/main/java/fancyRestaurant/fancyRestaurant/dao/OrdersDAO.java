package fancyRestaurant.fancyRestaurant.dao;

import fancyRestaurant.fancyRestaurant.model.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface OrdersDAO extends CrudRepository<Orders, Integer> {

    List<Orders> findByUserId(int userId);

}
