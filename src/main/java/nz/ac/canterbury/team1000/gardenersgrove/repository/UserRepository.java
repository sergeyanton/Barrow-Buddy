package nz.ac.canterbury.team1000.gardenersgrove.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;

import java.util.Optional;

/**
 * User repository interface that interacts with the user table in the H2 database. Spring Data
 * derived query methods are used instead of SQL. See
 * <a href="https://www.baeldung.com/spring-data-derived-queries">...</a> for info on how these work.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);

    void deleteById(Long userId);


    /**
     * Updates the user with the given email to the new user details.
     *
     * @param oldEmail the email of the user to update
     * @param newUser  the new user details
     */
    default void updateUserByEmail(String oldEmail, User newUser) {
        Optional<User> optionalUser = findByEmail(oldEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFname(newUser.getFname());
            user.setLname(newUser.getLname());
            user.setEmail(newUser.getEmail());
            user.setPassword(newUser.getPassword());
            user.setDateOfBirth(newUser.getDateOfBirth());
            save(user);
        }
    }
}
