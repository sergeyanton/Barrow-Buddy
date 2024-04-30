package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a reset password token entity in the application.
 * Inspiration from source: https://www.baeldung.com/spring-security-registration-i-forgot-my-password
 */
@Entity
public class ResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

    public ResetToken(User user, int expiry) {
        this.token = UUID.randomUUID().toString();
        this.user = user;
        setExpiryDate(LocalDateTime.now().plusMinutes(expiry));
    }

    public ResetToken() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpiryDate(LocalDateTime expiry) {
        this.expiryDate = expiry;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
}
