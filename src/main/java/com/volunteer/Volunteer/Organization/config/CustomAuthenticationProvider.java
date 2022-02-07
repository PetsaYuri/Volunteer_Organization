package com.volunteer.Volunteer.Organization.config;

import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String password = authentication.getCredentials().toString();
        Users user = usersRepository.findByUsername(username);
        Boolean istruepass = bcrypt.matches(password, user.getPassword());
        SecurityConfig.setRole(user.getRoles());

        if(user == null)    {
            throw new BadCredentialsException("Невідомий користувач: " + username);
        }

        if(!istruepass) {
            throw new BadCredentialsException("Невірний пароль");
        }



        UserDetails principal = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
        return new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
