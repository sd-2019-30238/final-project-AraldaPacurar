package fancyRestaurant.fancyRestaurant.dao;

import fancyRestaurant.fancyRestaurant.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RoleDAO extends CrudRepository<Role, Integer> {

    List<Role> findByRole(String role);
}
