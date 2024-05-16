package nz.ac.canterbury.team1000.gardenersgrove.service;

import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


/**
 * This class is responsible for handling the business logic for verification tokens.
 */
@Service
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    /**
     * Constructs a new VerificationTokenService with the specified VerificationTokenRepository.
     *
     * @param verificationTokenRepository the repository for verification tokens
     */
    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    /**
     * Retrieves the verification token associated with the given user ID.
     *
     * @param userId the ID of the user.
     * @return the verification token associated with the user if found, otherwise null.
     */
    public VerificationToken getVerificationTokenByUserId(Long userId) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUserId(userId);
        return verificationToken.orElse(null);
    }

    /**
     * Saves a new verificationToken object to the repository
     *
     * @param verificationToken VerificationToken object containing the token and userId
     */
    public void addVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    /**
     * Deletes the verification token associated with the given user ID.
     * Used when user is verified.
     *
     * @param userId ID of the user whose token is to be deleted.
     */
    @Transactional
    public void deleteVerificationTokenByUserId(Long userId) {
        verificationTokenRepository.deleteByUserId(userId);
    }

    /**
     * Retrieves the VerificationToken object associated with the given token string.
     * @param token String token value
     * @return VerificationToken object containing the token String and associated userId if found, else null
     */
    public VerificationToken getVerificationTokenByToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        return verificationToken.orElse(null);
    }


}

