package fancyRestaurant.fancyRestaurant.dao;

import fancyRestaurant.fancyRestaurant.model.Rights;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RightsDAO extends CrudRepository<Rights, Integer> {

    List<Rights> findByMyRight(String myRight);
}
