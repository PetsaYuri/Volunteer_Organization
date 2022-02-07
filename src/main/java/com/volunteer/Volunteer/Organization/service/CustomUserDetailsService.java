package com.volunteer.Volunteer.Organization.service;

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
        Users login = usersRepository.findByUsername(username);

        if (login == null)  {
            throw new UsernameNotFoundException("Невідомий користувач: " + username);
        }
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username(login.getUsername())
                .password(login.getPassword())
                .roles(login.getRoles())
                .build();
        return user;
    }
}
