package nz.ac.canterbury.seng302.gardenersgrove.repository;

import nz.ac.canterbury.seng302.gardenersgrove.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User repository interface that interacts with the user table in the H2 database.
 * Spring Data derived query methods are used instead of SQL.
 * See https://www.baeldung.com/spring-data-derived-queries for info on how these work.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);
}
