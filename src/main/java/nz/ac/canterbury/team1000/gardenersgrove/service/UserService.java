package nz.ac.canterbury.team1000.gardenersgrove.service;

import jakarta.servlet.http.HttpServletRequest;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class provides services related to user service, such as user registration, retrieval, and
 * authentication.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user.
     * 
     * @param user The user to register.
     */
    public void registerUser(User user) {
        userRepository.save(user);
    }

    /**
     * Retrieves a user by email and password.
     * 
     * @param email The email of the user.
     * @param password The password of the user.
     * @return The user if found, otherwise null.
     */
    public User getUserByEmailAndPassword(String email, String password) {
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        return user.orElse(null);
    }

    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }

    /**
     * Checks if an email is already in use.
     * 
     * @param email The email to check.
     * @return True if the email is already in use, otherwise false.
     */
    public boolean checkEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    /**
     * Finds a user by email.
     * 
     * @param email The email of the user to find.
     * @return The user if found, otherwise null.
     */
    public User findEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    /**
     * Retrieves the currently logged-in user.
     * 
     * @return The currently logged-in user's name if exists, otherwise null.
     */
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return findEmail(currentPrincipalName);
    }

    /**
     * Checks if a user is currently signed in.
     * 
     * @return True if a user is signed in, otherwise false.
     */
    public boolean isSignedIn() {
        return getLoggedInUser() != null;
    }

    public void updateUserByEmail(String oldEmail, User newUser) {
        userRepository.updateUserByEmail(oldEmail, newUser);
    }

    /**
     * Authenticate the user with the given authenticationManager
     * 
     * @param authenticationManager The authentication manager to use
     * @param user The user object to authenticate
     * @param request The HttpServletRequest corresponding to the request made to the server
     */
    public void authenticateUser(AuthenticationManager authenticationManager, User user,
            HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword(), user.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(token);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
        }
    }
}
