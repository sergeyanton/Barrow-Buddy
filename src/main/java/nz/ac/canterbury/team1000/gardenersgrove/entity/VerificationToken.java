package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    protected VerificationToken() {
    }

    public VerificationToken(Long userId, String token, LocalDateTime expiryDate) {
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
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

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
}
