package nz.ac.canterbury.team1000.gardenersgrove.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Custom Security Configuration Such functionality was previously handled by
 * WebSecurityConfigurerAdapter
 */
@Configuration
@EnableWebSecurity
@ComponentScan("com.baeldung.security")
public class SecurityConfiguration {

    /**
     * Our Custom Authentication Provider {@link CustomAuthenticationProvider}
     */
    @Autowired
    private CustomAuthenticationProvider authProvider;

    /**
     * Create an Authentication Manager with our {@link CustomAuthenticationProvider}
     * 
     * @param http http security configuration object from Spring
     * @return a new authentication manager
     * @throws Exception if the AuthenticationManager can not be built
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();

    }


    /**
     *
     * @param http http security configuration object from Spring (beaned in)
     * @return Custom SecurityFilterChain
     * @throws Exception if the SecurityFilterChain can not be built
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Allow h2 console through security. Note: Spring 6 broke the nicer way to do this (i.e.
        // how the authorisation is handled below)
        // See https://github.com/spring-projects/spring-security/issues/12546
        http.authorizeHttpRequests(auth -> auth.requestMatchers(AntPathRequestMatcher.antMatcher("/h2/**")).permitAll())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2/**")))
                .authorizeHttpRequests(requests -> requests
                        // Allow "/", "/register", and "/login" to anyone (permitAll) - Also allow
                        // access to
                        // stylesheets
                        .requestMatchers("/", "/hello", "/register", "/login", "/forgotPassword",
                            "/resetPassword", "/css/**", "/images/**", "/register/verification")
                        .permitAll()
                        // Only allow admins to reach the "/admin" page
                        .requestMatchers("/admin")
                        // note we do not need the "ROLE_" prefix as we are calling "hasRole()"
                        .hasRole("ADMIN")
                        // Any other request requires authentication
                        .anyRequest().authenticated())
                // Redirect to "/" when unauthenticated user tries to access a page that requires
                // login
                .formLogin(login -> login.loginPage("/").permitAll())
                // Define the logout page and invalidate the session and delete cookies
                // The logout page works with a GET request
                // Use Anthropic Opus to work out how to allow a GET request to logout the user
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).logoutSuccessUrl("/")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
