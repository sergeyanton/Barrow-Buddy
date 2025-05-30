package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class represents a verification token entity in the application.
 */
@Entity
@Table(name = "verification_token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime expiryDate;
    @Transient
    private String plainToken;

    protected VerificationToken() {
    }

    public VerificationToken(Long userId) {
        this.userId = userId;
        this.plainToken = generateToken();
        this.token = String.valueOf(this.plainToken.hashCode());
        this.expiryDate = calculateExpiryDate();
    }

    /**
     * Generates a random 6-digit number for the verification token
     *
     * @return the generated token
     */
    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        int code = secureRandom.nextInt(900000) + 100000; // Generates a random 6-digit number
        return String.valueOf(code);
    }

    /**
     * Calculates the expiry date of the token which is 10 minutes from the request time.
     *
     * @return the expiry date of the token
     */
    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(10);
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
    public String getPlainToken() {
        return plainToken;
    }

}
