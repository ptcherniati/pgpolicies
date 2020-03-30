package fr.ptcherniati.pg_policies.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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
                .antMatchers(HttpMethod.GET, "/api/v1/Produits/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/api/v1/Produits").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "//api/v1Produits/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/v1/Produits/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/Produits/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/Users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/Users").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/Users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/v1/Users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/Users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/Authorities/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/Authorities").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/Authorities/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/v1/Authorities/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/Authorities/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin()
                .usernameParameter("username") // default is username
                .passwordParameter("password") // default is password
                .loginPage("/static/index.html") // default is /login with an HTTP get
                .failureUrl("/static/index.html?failed") // default is /login?error
                .loginProcessingUrl("/static/index.html?ok=true");
    }

    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
    }
}
