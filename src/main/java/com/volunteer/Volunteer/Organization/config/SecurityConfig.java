package com.volunteer.Volunteer.Organization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    private static String role = "";

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
       http
                   .csrf().disable()
                   .authorizeRequests()
                   .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                   .antMatchers("/admint/**").hasRole("ADMIN")
                   .antMatchers("/**", "/login*").permitAll()
                   .antMatchers("/css/**", "/icon/**").permitAll()
                   .anyRequest().authenticated()

               .and()
                   .formLogin()
                   .loginPage("/login")
                   .loginProcessingUrl("/login")
                   .defaultSuccessUrl("/")
                   .failureUrl("/login?error=true")
               .and()
                   .logout()
                   .logoutUrl("/logout")
                   .permitAll()
                   .deleteCookies("JSESSIONID");

    }

    @Bean
    public PasswordEncoder passwordEncoder()  {
        return new BCryptPasswordEncoder();
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String Role) {
        role = Role;
    }
}
