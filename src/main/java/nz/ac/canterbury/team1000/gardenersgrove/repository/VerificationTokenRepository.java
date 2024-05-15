package nz.ac.canterbury.team1000.gardenersgrove.repository;

import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Verification Token repository accessor using Spring's @link{CrudRepository}. These (basic) methods are
 * provided for us without the need to write our own implementations
 */
@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByUserId(Long userId);
    Optional<VerificationToken> findByToken(String token);
    List<VerificationToken> findByExpiryDateBefore(LocalDateTime now);
}
