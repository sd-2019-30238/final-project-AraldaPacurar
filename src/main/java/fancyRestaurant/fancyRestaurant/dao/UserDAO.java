package fancyRestaurant.fancyRestaurant.dao;

import fancyRestaurant.fancyRestaurant.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserDAO extends CrudRepository<User, Integer>{

    List<User> findByUsername(String username);
    List<User> findByRoleId(int roleId);
}
