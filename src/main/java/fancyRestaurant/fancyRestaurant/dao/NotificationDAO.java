package fancyRestaurant.fancyRestaurant.dao;

import fancyRestaurant.fancyRestaurant.model.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface NotificationDAO extends CrudRepository<Notification, Integer> {

    List<Notification> findByUserId(int userId);
    List<Notification> findByOrderId(int orderId);
    void deleteByUserId(int userId);
    void deleteByOrderId(int orderId);
}
