package org.blogify.blogapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String USERS_SQL_QUERY = "select username, password, enabled from users where username = ?";
    private static final String AUTHORITIES_SQL_QUERY = "select users.username, authorities.authority " +
            "from users " +
            "inner join users_authorities on (users.id = users_authorities.user_id) " +
            "inner join authorities on (users_authorities.authority_id = authorities.id) " +
            "where users.username = ?";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Still disabled, but consider enabling it for production
                .headers(headers -> headers.frameOptions().disable()) // Still disabled for H2 console, not recommended for production
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/createNewPost/**", "/editPost/**", "/comment/**").hasRole("USER")
                        .requestMatchers("/deletePost/**").hasRole("USER") // Changed from ADMIN to USER as per original code
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .rememberMeParameter("remember-me")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery(USERS_SQL_QUERY);
        userDetailsManager.setAuthoritiesByUsernameQuery(AUTHORITIES_SQL_QUERY);
        return userDetailsManager;
    }
}