package org.blogify.blogapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final DataSource dataSource;
    private static final String USERS_SQL_QUERY = "select username, password, enabled from users where username = ?";
    private static final String AUTHORITIES_SQL_QUERY = "select users.username, authorities.authority " +
            "from users " +
            "inner join users_authorities on (users.id = users_authorities.user_id) " +
            "inner join authorities on (users_authorities.authority_id = authorities.id) " +
            "where users.username = ?;";

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .requestMatchers("/createNewPost/**", "/editPost/**", "/comment/**").hasRole("USER")
                .requestMatchers("/deletePost/**").hasRole("USER")
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").loginProcessingUrl("/login")
                .usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/").failureUrl("/login?error")
                .permitAll()
                .and()
                .rememberMe().rememberMeParameter("remember-me")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
                .sessionManagement().maximumSessions(1);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(USERS_SQL_QUERY);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(AUTHORITIES_SQL_QUERY);
        return jdbcUserDetailsManager;
    }
}
