package nz.ac.canterbury.team1000.gardenersgrove.repository;

import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.team1000.gardenersgrove.entity.WeatherData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * TODO JAVADOC
 */
@Repository
public interface WeatherRepository extends CrudRepository<WeatherData, Long> {
    Optional<WeatherData> findById(long id);

    @Query("SELECT w FROM WeatherData w WHERE w.gardenId = :gardenId")
    Optional<List<WeatherData>> findByGardenId(@Param("gardenId") Long gardenId);
}
