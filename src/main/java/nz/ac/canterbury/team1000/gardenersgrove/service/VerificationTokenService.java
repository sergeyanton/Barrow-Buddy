package nz.ac.canterbury.team1000.gardenersgrove.service;

import nz.ac.canterbury.team1000.gardenersgrove.entity.VerificationToken;
import nz.ac.canterbury.team1000.gardenersgrove.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationTokenService {
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken getVerificationTokenByUserId(String userId) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUserId(userId);
        return verificationToken.orElse(null);
    }

    public void deleteVerificationTokenByUserId(String userId) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUserId(userId);
        verificationToken.ifPresent(verificationTokenRepository::delete);
    }
    
    public VerificationToken addVerificationToken(VerificationToken verificationToken) {
        return verificationTokenRepository.save(verificationToken);
    }

}

