package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
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

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpiryDate(int expiry) {
        this.expiryDate = LocalDateTime.now().plusMinutes(expiry);
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
