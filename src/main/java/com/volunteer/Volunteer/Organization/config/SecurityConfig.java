package com.volunteer.Volunteer.Organization.config;

import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private UsersRepository usersRepository;

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
                   .antMatchers("/editor/**").hasAnyRole("editor", "admin")
                   .antMatchers("/**", "/login*").permitAll()
                   .antMatchers("/css/**", "/icon/**").permitAll()
                   .anyRequest().authenticated()

               .and()
                   .formLogin()
                   .loginPage("/login")
                   .loginProcessingUrl("/login")
                   .defaultSuccessUrl("/").successHandler(new AuthenticationSuccessHandler() {
           @Override
           public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
               UrlPathHelper helper = new UrlPathHelper();
               String context = helper.getContextPath(httpServletRequest);

               Users user = usersRepository.findByEmail(authentication.getName());
               switch (user.getRoles().getRole())  {
                   case "admin":
                       httpServletResponse.sendRedirect(context + "/admin/");
                       break;
                   case "user":
                       httpServletResponse.sendRedirect(context + "/user/");
                       break;
                   case "editor":
                       httpServletResponse.sendRedirect(context + "/editor/");
                       break;
                   default:
                       httpServletResponse.sendRedirect(context + "/");
               }
           }
       })
                   .failureUrl("/login?error=true")
               .and()
                   .logout()
               .logoutSuccessHandler(new LogoutSuccessHandler() {
           @Override
           public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
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
