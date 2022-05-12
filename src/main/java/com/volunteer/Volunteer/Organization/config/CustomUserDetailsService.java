package com.volunteer.Volunteer.Organization.config;

import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users login = usersRepository.findByEmail(username);

        if (login == null)  {
            throw new UsernameNotFoundException("Невідомий користувач: " + username);
        }
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username(login.getEmail())
                .password(login.getPassword())
                .roles(login.getRoles().getRole())
                .build();
        return user;
    }
}