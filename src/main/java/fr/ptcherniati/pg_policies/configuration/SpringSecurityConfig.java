package fr.ptcherniati.pg_policies.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    // Create 2 users for demo
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                /*.withUser("user").password(passwordEncoder().encode("password")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("password")).roles("USER", "ADMIN")*/;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Secure the endpoins with HTTP Basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/Produits/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/Produits").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/Produits/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/Produits/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/Produits/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/Users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/Users").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/Users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/Users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/Users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/Authorities/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/Authorities").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/Authorities/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/Authorities/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/Authorities/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}
