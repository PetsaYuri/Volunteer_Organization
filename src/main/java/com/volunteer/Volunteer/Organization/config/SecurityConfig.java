package com.volunteer.Volunteer.Organization.config;

import com.volunteer.Volunteer.Organization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
       http
                   .csrf().disable()
                   .authorizeRequests()
                   .antMatchers("/user/**").hasAnyRole("user", "admin")
                   .antMatchers("/admin/**").hasRole("admin")
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
               .logoutSuccessHandler(new LogoutSuccessHandler() {
           @Override
           public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
               UserService.setCurrentRole("guest");

               UrlPathHelper helper = new UrlPathHelper();
               String context = helper.getContextPath(request);
               response.sendRedirect(context + "/login?logout");
           }
       });
    }

    @Bean
    public PasswordEncoder passwordEncoder()  {
        return new BCryptPasswordEncoder();
    }
}
