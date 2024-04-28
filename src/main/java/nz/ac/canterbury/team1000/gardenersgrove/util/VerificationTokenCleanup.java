package nz.ac.canterbury.team1000.gardenersgrove.util;

import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserRepository userRepository;


    /**
     * Deletes all expired verification tokens from the database every minute.
     * If the user's account is not verified, the user account is also deleted.
     */
    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupExpiredTokens() {
        List<VerificationToken> expiredTokens = verificationTokenRepository.findByExpiryDateBefore(LocalDateTime.now());
        logger.info("Found {} expired tokens", expiredTokens.size());
        for (VerificationToken token : expiredTokens) {
            if (!token.isVerified()) {
                logger.info("Deleting user with id {}", token.getUserId());
                userRepository.deleteById(token.getUserId());
            }
        }
        verificationTokenRepository.deleteAll(expiredTokens);
    }

}
