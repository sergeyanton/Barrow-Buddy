package nz.ac.canterbury.team1000.gardenersgrove.repository;

import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Weather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Weather repository accessor using Spring's @link{CrudRepository}.
 */
@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
    List<Weather> findByGardenId(long gardenId);
}
