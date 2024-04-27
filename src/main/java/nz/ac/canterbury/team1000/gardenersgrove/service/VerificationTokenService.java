package nz.ac.canterbury.team1000.gardenersgrove.service;

import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

// TODO Documentation

/**
 * This class is responsible for handling the business logic for verification tokens.
 */
@Service
public class VerificationTokenService {
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken getVerificationTokenByUserId(Long userId) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUserId(userId);
        return verificationToken.orElse(null);
    }

    public void deleteVerificationTokenByUserId(Long userId) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUserId(userId);
        verificationToken.ifPresent(verificationTokenRepository::delete);
    }

    public void addVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }


}

