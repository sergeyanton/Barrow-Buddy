package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tab_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fname;

    @Column(nullable = true)
    private String lname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String dateOfBirth;


    protected User() {}

    public User(String fname, String lname, String password, String email, String dateOfBirth) {
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
}
