package org.blogify.blogapp.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import javax.sql.DataSource;

public class CustomAuthenticationConfigurer {

    private final DataSource dataSource;

    public CustomAuthenticationConfigurer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> jdbcConfigurer =
                new JdbcUserDetailsManagerConfigurer<>();

        jdbcConfigurer.dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from users where username = ?")
                .authoritiesByUsernameQuery("select users.username, authorities.authority " +
                        "from users " +
                        "inner join users_authorities on (users.id = users_authorities.user_id) " +
                        "inner join authorities on (users_authorities.authority_id = authorities.id) " +
                        "where users.username = ?");

        authenticationManagerBuilder.apply(jdbcConfigurer);
    }
}
