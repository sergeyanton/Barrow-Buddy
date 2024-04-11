package nz.ac.canterbury.team1000.gardenersgrove.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.service.UserService;


/**
 * Custom Authentication Provider class, to allow for handling authentication in any way we see fit.
 * In this case using our existing {@link User}
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    /**
     * Autowired user service for custom authentication using our own user objects
     */
    @Autowired
    private UserService userService;

    public CustomAuthenticationProvider() {
        super();
    }

    /**
     * Custom authentication implementation
     * 
     * @param authentication An implementation object that must have non-empty email (name) and
     *        password (credentials)
     * @return A new {@link UsernamePasswordAuthenticationToken} if email and password are valid
     *         with users authorities
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        String email = String.valueOf(authentication.getName());
        String password = String.valueOf(authentication.getCredentials());

        User u = userService.getUserByEmailAndPassword(email, password);
        return new UsernamePasswordAuthenticationToken(u.getEmail(), null, u.getAuthorities());
    }

    /**
     * Authentication boilerplate
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
