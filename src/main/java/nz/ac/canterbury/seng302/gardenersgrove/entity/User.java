package nz.ac.canterbury.seng302.gardenersgrove.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column()
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Authority> userRoles;

    protected User() {}

    public User(String fname, String lname, String email, String password, String dateOfBirth) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public void grantAuthority(String authority) {
        if ( userRoles == null )
            userRoles = new ArrayList<>();

        userRoles.add(new Authority(authority));
    }

    public List<GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.userRoles.forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority.getRole())));
        return authorities;
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
