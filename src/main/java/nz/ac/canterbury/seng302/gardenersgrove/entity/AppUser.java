package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fname;

    //make false and add constructor
    @Column(nullable = false)
    private String lname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    //make false and add constructor
    @Column(nullable = false)
    private String dateOfBirth;

    protected AppUser() {}

    public AppUser(String fname, String lname, String password, String email, String dateOfBirth) {
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
