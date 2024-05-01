package nz.ac.canterbury.team1000.gardenersgrove.service;

import nz.ac.canterbury.team1000.gardenersgrove.entity.ResetToken;
import nz.ac.canterbury.team1000.gardenersgrove.repository.ResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResetTokenService {

    private ResetTokenRepository resetTokenRepository;

    @Autowired
    public ResetTokenService(ResetTokenRepository resetTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
    }

    public ResetToken getResetToken(String token) {
        Optional<ResetToken> resetToken = resetTokenRepository.findByToken(token);
        return resetToken.orElse(null);
    }

    public void deleteResetToken(String token) {
        Optional<ResetToken> resetToken = resetTokenRepository.findByToken(token);
        resetToken.ifPresent(resetTokenRepository::delete);
    }

    public void addResetToken(ResetToken resetToken) {
        resetTokenRepository.save(resetToken);
    }

}
