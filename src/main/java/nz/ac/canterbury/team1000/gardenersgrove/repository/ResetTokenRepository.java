package nz.ac.canterbury.team1000.gardenersgrove.repository;

import nz.ac.canterbury.team1000.gardenersgrove.entity.ResetToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Reset Token repository accessor using Spring's @link{CrudRepository}. These (basic) methods are
 * provided for us without the need to write our own implementations
 */
@Repository
public interface ResetTokenRepository extends CrudRepository<ResetToken, Long> {
    Optional<ResetToken> findByToken(String token);

    List<ResetToken> findByExpiryDateBefore(LocalDateTime now);
}
