package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a reset password token entity in the application.
 * Inspiration from source: https://www.baeldung.com/spring-security-registration-i-forgot-my-password
 */
@Entity
@Table(name = "reset_token")
public class ResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    public ResetToken(Long userId, int expiry) {
        this.userId = userId;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = LocalDateTime.now().plusMinutes(expiry);
    }

    public ResetToken() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setExpiryDate(LocalDateTime expiry) {
        this.expiryDate = expiry;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
}
