package nz.ac.canterbury.team1000.gardenersgrove.util;

import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is responsible for cleaning up expired verification tokens from the database.
 * Deletes all expired tokens every minute.
 * GPT used.
 */
@EnableScheduling
@Component
public class VerificationTokenCleanup {
    Logger logger = LoggerFactory.getLogger(VerificationTokenCleanup.class);
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    /**
     * Deletes all expired verification tokens from the database every minute.
     */
    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupExpiredTokens() {
        List<VerificationToken> expiredTokens = verificationTokenRepository.findByExpiryDateBefore(LocalDateTime.now());
        logger.info("Found " + expiredTokens.size() + " expired tokens");
        verificationTokenRepository.deleteAll(expiredTokens);
    }

}
