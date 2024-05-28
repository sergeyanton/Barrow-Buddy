package nz.ac.canterbury.team1000.gardenersgrove.entity;

import jakarta.persistence.*;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a user entity in the application.
 */
@Entity
@Table(name = "tab_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fname;

    @Column
    private String lname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private LocalDate dateOfBirth;

    @Column
    private String picturePath;

    @Column()
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Authority> userRoles;

    /**
     * The default constructor that is required.
     */
    protected User() {}


    /**
     * Constructs a User object with the given details.
     * 
     * @param fname The first name of the user.
     * @param lname The last name of the user.
     * @param email The email address of the user.
     * @param password The password of the user.
     * @param dateOfBirth The date of birth of the user.
     */
    public User(String fname, String lname, String email, String password, LocalDate dateOfBirth, String picturePath) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.picturePath = picturePath;
    }

    /**
     * Grants a new authority to the user.
     * 
     * @param authority The authority to grant.
     */
    public void grantAuthority(String authority) {
        if (userRoles == null)
            userRoles = new ArrayList<>();

        userRoles.add(new Authority(authority));
    }

    /**
     * Retrieves authorities granted to the user.
     * 
     * @return A list of authorities granted to the user.
     */
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.userRoles.forEach(
                authority -> authorities.add(new SimpleGrantedAuthority(authority.getRole())));
        return authorities;
    }

    /**
     * Gets the user's full name, with a space between the first name and last name.
     * If the user has no last name, it returns the first name.
     *
     * @return the user's full name
     */
    public String getFullName() {
        if (lname == null) {
            return fname;
        }
        return fname + " " + lname;
    }

    public Long getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        if (lname == null) {
            return "";
        }
        return lname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Returns the date of birth as a string in the format DD/MM/YYYY.
     * 
     * @return The date of birth as a string in the format DD/MM/YYYY.
     */
    public String getDateOfBirthString() {
        return dateOfBirth.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", fname=" + fname + ", lname=" + lname + ", email=" + email +
                ", dob="  + dateOfBirth + ", picturePath=" + picturePath + ", password=" + password + "}";
    }
}
