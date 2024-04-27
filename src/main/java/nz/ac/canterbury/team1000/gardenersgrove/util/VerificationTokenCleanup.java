package nz.ac.canterbury.team1000.gardenersgrove.util;

import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is responsible for cleaning up expired verification tokens from the database.
 * Deletes all expired tokens every minute.
 * <p>
 * GPT used
 */
@Component
public class VerificationTokenCleanup {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    /**
     * Deletes all expired verification tokens from the database every minute.
     */
    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupExpiredTokens() {
        List<VerificationToken> expiredTokens = verificationTokenRepository.findByExpiryDateBefore(LocalDateTime.now());
        verificationTokenRepository.deleteAll(expiredTokens);
    }

}
